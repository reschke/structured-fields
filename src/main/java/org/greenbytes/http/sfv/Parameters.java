package org.greenbytes.http.sfv;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the Parameters of an Item or an Inner List.
 * 
 * @see <a href=
 *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#param">Section
 *      3.1.2 of draft-ietf-httpbis-header-structure-18</a>
 */
public class Parameters implements Type<Map<String, Item<? extends Object>>> {

    private final Map<String, Item<? extends Object>> value;

    public static final Parameters EMPTY = new Parameters(Collections.emptyMap());

    private Parameters(Map<String, Item<? extends Object>> value) {
        this.value = Collections.unmodifiableMap(Objects.requireNonNull(value, "value must not be null"));
        checkKeys(this.value);
    }

    /**
     * Creates a {@link Parameters} instance representing the specified
     * {@code Map<String, Item>} value.
     * <p>
     * Note that the {@link Map} implementation that is used here needs to
     * iterate predictably based on insertion order, such as
     * {@link java.util.LinkedHashMap}.
     * 
     * @param value
     *            a {@code Map<String, Item>} value
     * @return a {@link Parameters} representing {@code value}.
     */
    public static Parameters valueOf(Map<String, Item<? extends Object>> value) {
        return new Parameters(value);
    }

    @Override
    public Map<String, Item<? extends Object>> get() {
        return value;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        for (Map.Entry<String, Item<? extends Object>> e : value.entrySet()) {
            sb.append(';').append(e.getKey());
            if (!(e.getValue().get().equals(Boolean.TRUE))) {
                sb.append('=');
                e.getValue().serializeTo(sb);
            }
        }
        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    private static void checkKeys(Map<String, Item<? extends Object>> map) {
        for (String key : map.keySet()) {
            checkKey(key);
        }
    }

    private static void checkKey(String value) {
        if (value.length() == 0) {
            throw new IllegalArgumentException("Key can not be empty");
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if ((i == 0 && (c != '*' && !isLcAlpha(c)))
                    || !(isLcAlpha(c) || isDigit(c) || c == '_' || c == '-' || c == '.' || c == '*')) {
                throw new IllegalArgumentException(
                        String.format("Invalid character in key at position %d: '%c' (0x%04x)", i, c, (int) c));
            }
        }
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isLcAlpha(char c) {
        return (c >= 'a' && c <= 'z');
    }
}
