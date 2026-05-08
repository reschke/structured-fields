package org.greenbytes.http.sfv;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
<<<<<<< api
import java.util.Objects;
=======
>>>>>>> main
import java.util.function.Function;

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
    public static Dictionary of(Map<String, ListElement<?>> value) {
        return new Dictionary(value);
    }

    /**
     * Creates a {@link Dictionary} instance representing the values
     * (key/value pairs)
     *
     * @param obs
     *            a sequence of key/value pairs
     * @return a {@link Dictionary} representing the {@code values}.
     */
    public static Dictionary valueOf(Object... obs) {
        if (obs.length % 2 != 0) {
            throw new IllegalArgumentException("requires even number of arguments, got: " + obs.length);
        } else {
            Map<String, ListElement<?>> map = new LinkedHashMap<>();
            for (int i = 0; i < obs.length; i += 2) {
                String key = obs[i].toString();
                Object value = obs[i + 1];
                if (map.containsKey(key)) {
                    throw new IllegalArgumentException("key " + key + " already exists");
                }
                if (value instanceof ListElement) {
                    map.put(key, (ListElement<?>) value);
                } else {
                    map.put(key, Utils.asItem(obs[i + 1]));
                }
            }
            return of(map);
        }
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
            ListElement<?> dict = e.getValue();

            sb.append(name);
            if (Boolean.TRUE.equals(dict.get())) {
                dict.getParams().serializeTo(sb);
            } else {
                sb.append("=");
                dict.serializeTo(sb);
            }
        }

        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    @Override
    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel, Function<Class, String> formatter) {
        String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
        String classn = formatter.apply(this.getClass());
        sb.append(indent).append(serialize()).append(classn).append("\n");
        for (Map.Entry<String, ListElement<?>> e : value.entrySet()) {
            sb.append(indent +" ").append(e.getKey()).append(" -> \n");
            e.getValue().serializeToForDebug(sb, indentLevel + 2, formatter);
        }
        return sb;
    }
<<<<<<< api

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Dictionary)) {
            return false;
        } else {
            Dictionary that = (Dictionary) o;
            return Objects.equals(value, that.value);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
=======
>>>>>>> main
}
