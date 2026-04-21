package org.greenbytes.http.sfv;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * Common utility methods.
 */
public class Utils {

    private Utils() {
    }

    /** check for character to be a decimal digit */
    protected static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /** check for character to be lowercase alphanumeric */
    protected static boolean isLcAlpha(char c) {
        return (c >= 'a' && c <= 'z');
    }

    /** check for character to be alphanumeric */
    protected static boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    /**
     * Checks a key for validity
     * @param value to check
     * @return checked value
     */
    protected static String checkKey(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Key can not be null or empty");
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if ((i == 0 && (c != '*' && !isLcAlpha(c)))
                    || !(isLcAlpha(c) || isDigit(c) || c == '_' || c == '-' || c == '.' || c == '*')) {
                throw new IllegalArgumentException(
                        String.format("Invalid character in key at position %d: '%c' (0x%04x)", i, c, (int) c));
            }
        }
        return value;
    }

    /**
     * Checks all keys in map for validity
     * @param value map to check
     * @return checked map
     */
    protected static Map<String, ListElement<?>> checkKeys(Map<String, ListElement<?>> value) {
        for (String key : Objects.requireNonNull(value, "value must not be null").keySet()) {
            checkKey(key);
        }
        return value;
    }

    /**
     * Converts from native Java object
     * @param o to convert
     * @return convert to {@linkplain Item}
     */
    protected static Item<?> asBareItem(Object o) {
        if (o instanceof Item) {
            if (o instanceof Parameterizable) {
                Parameterizable p = ((Parameterizable)o);
                if (!p.getParams().isEmpty()) {
                    throw new IllegalArgumentException("Can't map value " + o + " (" + o.getClass() + "): carries parameters.");
                }
            }
            return (Item<?>) o;
        } else if (o instanceof Integer) {
            return IntegerItem.valueOf(((Integer) o).longValue());
        } else if (o instanceof Long) {
            return IntegerItem.valueOf((Long) o);
        } else if (o instanceof String) {
            return StringItem.valueOf((String) o);
        } else if (o instanceof Boolean) {
            return BooleanItem.valueOf((Boolean) o);
        } else if (o instanceof byte[]) {
            return ByteSequenceItem.valueOf((byte[]) o);
        } else if (o instanceof BigDecimal) {
            return DecimalItem.valueOf((BigDecimal)o);
        } else if (o instanceof Double) {
            return DecimalItem.valueOf(BigDecimal.valueOf((Double)o));
        } else if (o instanceof Float) {
            return DecimalItem.valueOf(BigDecimal.valueOf((Float)o));
        } else {
            throw new IllegalArgumentException("Can't map value " + o.toString() + " (" + o.getClass() + "): carries parameters.");
        }
    }
}
