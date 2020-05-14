package org.greenbytes.http.sfv;

import java.util.Collections;
import java.util.Map;

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
        this.value = Collections.unmodifiableMap(Utils.checkKeys(value));
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
}
