package org.greenbytes.http.sfv;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Common utility methods.
 */
public class Utils {

    private Utils() {
    }

    /**
     * Check for character to be a decimal digit.
     * @param c character to check
     * @return {@code true} if and only if a digit
     */
    protected static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Check for character to be lowercase ASCII alpha character.
     * @param c character to check
     * @return {@code true} if and only if lowercase ASCII alpha
     */
    protected static boolean isLcAlpha(char c) {
        return (c >= 'a' && c <= 'z');
    }

    /**
     * Check for character to be lowercase or uppercase ASCII alpha character.
     * @param c character to check
     * @return {@code true} if and only if lowercase or uppercase ASCII alpha
     */
    protected static boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    /**
     * Checks <a href="https://www.rfc-editor.org/rfc/rfc9651.html#dictionaries">key</a> for validity.
     * @param value to check
     * @return checked value
     * @throws IllegalArgumentException when invalid
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
     * Checks all <a href="https://www.rfc-editor.org/rfc/rfc9651.html#dictionaries">keys</a>
     * in {@linkplain Map} for validity
     * @param value map to check
     * @return checked map
     * @throws IllegalArgumentException when invalid key found
     */
    protected static Map<String, ListElement<?>> checkKeys(Map<String, ListElement<?>> value) {
        for (String key : Objects.requireNonNull(value, "value must not be null").keySet()) {
            checkKey(key);
        }
        return value;
    }

    /**
     * Converts an {@linkplain Object} to an {@link Item} (on a best-effort basis).
     * @param o to convert
     * @return converted to {@linkplain Item}
     * @throws IllegalArgumentException when it can't be converted
     */
    protected static Item<?> asBareItem(Object o) {
        if (o instanceof Item) {
            if (o instanceof Parameterizable) {
                Parameterizable p = ((Parameterizable)o);
                if (!p.getParams().isEmpty()) {
                    throw new IllegalArgumentException("Can't map value " + o + " (" + o.getClass() + "): carries parameters.");
                }
            }
        }
        return asItem(o);
    }

    /**
     * Converts an {@linkplain Object} to an {@link Item} (on a best-effort basis).
     * <p>
     * Same as {@linkplain #asBareItem(Object)}, but allowing {@linkplain Parameters}
     * @param o to convert
     * @return converted to {@linkplain Item}
     * @throws IllegalArgumentException when it can't be converted
     */
    protected static Item<?> asItem(Object o) {
        if (o instanceof Item) {
            return (Item<?>) o;
        } else if (o instanceof Integer) {
            return IntegerItem.of(((Integer) o).longValue());
        } else if (o instanceof Long) {
            return IntegerItem.of((Long) o);
        } else if (o instanceof String) {
            try {
                return StringItem.of((String) o);
            } catch (IllegalArgumentException ia) {
                return DisplayStringItem.valueOf((String) o);
            }
        } else if (o instanceof Boolean) {
            return BooleanItem.of((Boolean) o);
        } else if (o instanceof byte[]) {
            return ByteSequenceItem.valueOf((byte[]) o);
        } else if (o instanceof BigDecimal) {
            return DecimalItem.valueOf((BigDecimal)o);
        } else if (o instanceof Double) {
            return DecimalItem.valueOf(BigDecimal.valueOf((Double)o));
        } else if (o instanceof Float) {
            return DecimalItem.valueOf(BigDecimal.valueOf((Float)o));
        } else {
            throw new IllegalArgumentException("Can't map value " + o.toString() + " (" + o.getClass() + ")");
        }
    }

    /**
     * Converts an {@linkplain Object} to an {@linkplain List} of {@linkplain Item}s
     * (on a best-effort basis).
     * Same as {@linkplain #asItem(Object)}, but also allowing {@linkplain InnerList}
     * @param o to convert
     * @return convert to {@linkplain ListElement}
     * @throws IllegalArgumentException when it can't be converted
     */
    protected static ListElement<?> asListElement(Object o) {
        if (o instanceof InnerList) {
            return (InnerList) o;
        } else {
            return asItem(o);
        }
    }
}
