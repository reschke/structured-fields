package org.greenbytes.http.sfv;

import java.util.Collections;
import java.util.Map;

/**
 * Represents a Dictionary.
 * 
 * @see <a href=
 *      "https://www.rfc-editor.org/rfc/rfc9651.html#dictionary">Section 3.2 of
 *      RFC 9651</a>
 */
public class Dictionary implements Type<Map<String, ListElement<?>>> {

    private final Map<String, ListElement<?>> value;

    private Dictionary(Map<String, ListElement<?>> value) {
        this.value = Collections.unmodifiableMap(Utils.checkKeys(value));
    }

    /**
     * Creates a {@link Dictionary} instance representing the specified
     * {@code Map<String, Item>} value.
     * <p>
     * Note that the {@link Map} implementation that is used here needs to
     * iterate predictably based on insertion order, such as
     * {@link java.util.LinkedHashMap}.
     * 
     * @param value
     *            a {@code Map<String, Item>} value
     * @return a {@link Dictionary} representing {@code value}.
     */
    public static Dictionary valueOf(Map<String, ListElement<?>> value) {
        return new Dictionary(value);
    }

    @Override
    public Map<String, ListElement<?>> get() {
        return value;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        String separator = "";

        for (Map.Entry<String, ListElement<?>> e : value.entrySet()) {
            sb.append(separator);
            separator = ", ";

            String name = e.getKey();
            ListElement<?> value = e.getValue();

            sb.append(name);
            if (Boolean.TRUE.equals(value.get())) {
                value.getParams().serializeTo(sb);
            } else {
                sb.append("=");
                value.serializeTo(sb);
            }
        }

        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }
}
