package org.greenbytes.http.sfv;

import java.util.List;
import java.util.Objects;

/**
 * Represents a List.
 * 
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#list">Section 3.1
 *      of RFC 9651</a>
 */
public class OuterList implements Type<List<ListElement<?>>> {

    private final List<ListElement<?>> value;

    private OuterList(List<ListElement<?>> value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    /**
     * Creates an {@link OuterList} instance representing the specified
     * {@code List<Item>} value.
     * 
     * @param value
     *            a {@code List<Item>} value.
     * @return a {@link OuterList} representing {@code value}.
     */
    public static OuterList valueOf(List<ListElement<?>> value) {
        return new OuterList(value);
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        String separator = "";

        for (ListElement<?> i : value) {
            sb.append(separator);
            separator = ", ";
            i.serializeTo(sb);
        }

        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    @Override
    public List<ListElement<?>> get() {
        return value;
    }
}
