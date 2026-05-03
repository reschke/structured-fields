package org.greenbytes.http.sfv;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * Creates an {@link InnerList} instance representing the specified
     * {@code Item} values.
     *
     * @param values
     *            {@code Item} values.
     * @return a {@link InnerList} representing {@code values}.
     */
    public static InnerList of(Item<?>... values) {
        return of(Arrays.stream(values).collect(Collectors.toList()));
    }

    /**
     * Creates an {@link InnerList} instance representing the specified
     * {@linkplain Object} values after best-effort conversion to {@linkplain Item}s.
     *
     * @param values {@link Object}s to populate the list with
     * @return a {@link InnerList} representing {@code values}.
     */
    public static InnerList valueOf(Object... values) {
        return of(Arrays.stream(values).map(v -> Utils.asItem(v))
                .collect(Collectors.toList()));
    }

    @Override
    public InnerList withParams(Parameters params) {
        return new InnerList(this.value, Objects.requireNonNull(params, "params must not be null"));
    }

    @Override
    public InnerList withParamValuesOf(Object... obs) {
        return new InnerList(this.value, Parameters.valueOf(obs));
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
