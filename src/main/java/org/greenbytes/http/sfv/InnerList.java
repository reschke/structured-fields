package org.greenbytes.http.sfv;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents an Inner List.
 *
 * @see <a href=
 *      "https://www.rfc-editor.org/rfc/rfc9651.html#inner-list">Section 3.1.1
 *      of RFC 9651</a>
 */
public class InnerList implements ListElement<List<Item<?>>>, Parameterizable<List<Item<?>>> {

    private final List<Item<?>> value;
    private final Parameters params;

    private InnerList(List<Item<?>> value, Parameters params) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    /**
     * @deprecated  - use {@linkplain #of(List)} instead.
     */
    @Deprecated
    public static InnerList valueOf(List<Item<?>> value) {
        return new InnerList(value, Parameters.EMPTY);
    }

    /**
     * Creates an {@link InnerList} instance representing the specified
     * {@code List<Item>} value.
     *
     * @param value
     *            a {@code List<Item>} value.
     * @return a {@link InnerList} representing {@code value}.
     */
    public static InnerList of(List<Item<?>> value) {
        return new InnerList(value, Parameters.EMPTY);
    }

    @Override
    public InnerList withParams(Parameters params) {
        return new InnerList(this.value, Objects.requireNonNull(params, "params must not be null"));
    }

    private StringBuilder serializeToNoParams(StringBuilder sb) {
        String separator = "";

        sb.append('(');

        for (Item<?> i : value) {
            sb.append(separator);
            separator = " ";
            i.serializeTo(sb);
        }

        sb.append(')');

        return sb;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
       return params.serializeTo(serializeToNoParams(sb));
    }

    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel, Function<Class, String> formatter) {
        String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
        String classn = formatter.apply(this.getClass());

        sb.append(indent);
        sb = serializeToNoParams(sb);
        sb.append(classn).append("\n");

        for (ListElement<?> le : value) {
            sb.append(le.serializeToForDebug(new StringBuilder(), indentLevel + 2, formatter));
        }

        return params.serializeToForDebug(sb, indentLevel + 2, formatter);
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    @Override
    public List<Item<?>> get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InnerList)) {
            return false;
        } else {
            InnerList innerList = (InnerList) o;
            return Objects.equals(value, innerList.value) && Objects.equals(params, innerList.params);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, params);
    }
}
