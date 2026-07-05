package org.greenbytes.http.sfv;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a List.
 * 
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#list">Section 3.1
 *      of RFC 9651</a>
 */
public class SfList implements Type<List<ListElement<?>>> {

    private final List<ListElement<?>> value;

    private SfList(List<ListElement<?>> value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    @Override
    public SfDataType getType() {
        return SfDataType.LIST;
    }

    /**
     * Implementation of "Parsing a List"
     *
     * @param input values of field values.
     * @return result of parse as {@link SfList}.
     *
     * @see <a href=
     *      "https://www.rfc-editor.org/rfc/rfc9651.html#parse-list">Section
     *      4.2.1 of RFC 9651</a>
     */
    public static SfList parse(List<String> input) {
        return new Parser(input).parseList();
    }

    /**
     * @see #parse(List<String>)
     */
    public static SfList parse(String input) {
        return new Parser(input).parseList();
    }

    /**
     * Creates an {@link SfList} instance representing the specified
     * {@linkplain Object} values after best-effort conversion to {@linkplain Item}.
     *
     * @param values {@link Object}s to populate the list with
     * @return a {@link SfList} representing {@code values}.
     */
    public static SfList valueOf(Object... values) {
        return of(Arrays.stream(values).map(Utils::asListElement).collect(Collectors.toList()));
    }

    /**
     * Creates an {@link SfList} instance representing the specified
     * {@linkplain List} value.
     *
     * @param value
     *            a {@code List<Item>} value.
     * @return a {@link SfList} representing {@code value}.
     */
    public static SfList of(List<ListElement<?>> value) {
        return new SfList(value);
    }

    /**
     * Creates an {@link SfList} instance representing the specified
     * {@code ListElement<Item>} values.
     *
     * @param values
     *            {@code ListItem<Item>} values.
     * @return a {@link SfList} representing {@code values}.
     */
    public static SfList of(ListElement<?>... values) {
        return of(Arrays.stream(values).collect(Collectors.toList()));
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
    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel, Function<Class, String> formatter) {
        String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
        String classn = formatter.apply(this.getClass());
        sb.append(indent).append(serialize()).append(classn).append("\n");

        for (ListElement<?> le : value) {
            sb.append(le.serializeToForDebug(new StringBuilder(), indentLevel + 2, formatter));
        }

        return sb;
    }

    @Override
    public List<ListElement<?>> get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SfList)) {
            return false;
        } else {
            SfList list = (SfList) o;
            return Objects.equals(value, list.value);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
