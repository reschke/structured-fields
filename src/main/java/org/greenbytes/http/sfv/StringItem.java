package org.greenbytes.http.sfv;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a String.
 * 
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#string">Section
 *      3.3.3 of RFC 9651</a>
 */
public class StringItem implements Item<String> {

    private final String value;
    private final Parameters params;

    private StringItem(String value, Parameters params) {
        this.value = checkParam(Objects.requireNonNull(value, "value must not be null"));
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    @Override
    public SfDataType getType() {
        return SfDataType.STRING;
    }

    /**
    * Creates a {@link StringItem} instance representing the specified
    * {@code String} value.
    *
    * @param value
    *            a {@code String} value.
    * @return a {@link StringItem} representing {@code value}.
    */
    public static StringItem of(String value) {
        return new StringItem(value, Parameters.EMPTY);
    }

    @Override
    public StringItem withParams(Parameters params) {
        return new StringItem(this.value, Objects.requireNonNull(params, "params must not be null"));
    }

    @Override
    public StringItem withParamValuesOf(Object... obs) {
        return new StringItem(this.value, Parameters.valueOf(obs));
    }

    @Override
    public Parameters params() {
        return params;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        sb.append('"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' || c == '"') {
                sb.append('\\');
            }
            sb.append(c);
        }
        sb.append('"');
        params.serializeTo(sb);
        return sb;
    }

    @Override
    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel, Function<Class, String> formatter) {
        String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
        String classn = formatter.apply(this.getClass());

        sb = sb.append(indent).append(value).append(classn).append("\n");
        sb = params.serializeToForDebug(sb, indentLevel + 2, formatter);
        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder(2 + value.length())).toString();
    }

    @Override
    public String get() {
        return this.value;
    }

    @Override
    public String stringValue() {
        return this.value;
    }

    private static String checkParam(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c < 0x20 || c >= 0x7f) {
                throw new IllegalArgumentException(
                        String.format("Invalid character in String at position %d: '%c' (0x%04x)", i, c, (int) c));
            }
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StringItem)) {
            return false;
        } else {
            StringItem that = (StringItem) o;
            return Objects.equals(value, that.value) && Objects.equals(params, that.params);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, params);
    }
}
