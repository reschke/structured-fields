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
    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel) {
        String s = String.format("%" + indentLevel + "s", "");
        sb.append(s).append(serialize()).append(" (").append(this.getClass().getSimpleName()).append(")\n");

        for (ListElement<?> le : value) {
            sb.append(le.serializeToForDebug(new StringBuilder(), indentLevel + 2));
        }

        return sb;
    }

    @Override
    public List<ListElement<?>> get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OuterList)) {
            return false;
        } else {
            OuterList list = (OuterList) o;
            return Objects.equals(value, list.value);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
