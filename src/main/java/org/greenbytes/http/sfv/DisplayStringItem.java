package org.greenbytes.http.sfv;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a Display String.
 *
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#displaystring">Section
 *      3.3.8 of RFC 9651</a>
 */
public class DisplayStringItem implements Item<String> {

    private final String value;
    private final Parameters params;

    private DisplayStringItem(String value, Parameters params) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    @Override
    public SfDataType getType() {
        return SfDataType.DISPLAYSTRING;
    }

    /**
     * Creates a {@link DisplayStringItem} instance representing the specified
     * {@code String} value.
     * 
     * @param value
     *            a {@code String} value.
     * @return a {@link DisplayStringItem} representing {@code value}.
     */
    public static DisplayStringItem valueOf(String value) {
        return new DisplayStringItem(value, Parameters.EMPTY);
    }

    @Override
    public DisplayStringItem withParams(Parameters params) {
        return new DisplayStringItem(this.value, Objects.requireNonNull(params, "params must not be null"));
    }

    @Override
    public DisplayStringItem withParamValuesOf(Object... obs) {
        return new DisplayStringItem(this.value, Parameters.valueOf(obs));
    }

    @Override
    public Parameters params() {
        return params;
    }

    private StringBuilder serializeToNoParams(StringBuilder sb) {
        sb.append("%\"");
        byte[] octets = value.getBytes(StandardCharsets.UTF_8);
        for (byte b : octets) {
            if (b == 0x25 || b == 0x22 || b <= 0x1f || b == 0x7f) {
                sb.append('%');
                sb.append(Character.forDigit((b >> 4) & 0xf, 16));
                sb.append(Character.forDigit(b & 0xf, 16));
            } else {
                sb.append((char) b);
            }
        }
        sb.append('"');
        return sb;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        sb = serializeToNoParams(sb);
        sb = params.serializeTo(sb);
        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder(2 + value.length())).toString();
    }

    @Override
    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel, Function<Class, String> formatter) {
        String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
        String classn = formatter.apply(this.getClass());

        sb = sb.append(indent);
        sb = serializeToNoParams(sb);
        sb.append(classn).append("\n");
        sb = params.serializeToForDebug(sb, indentLevel + 2, formatter);
        return sb;
    }

    @Override
    public String get() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DisplayStringItem)) {
            return false;
        } else {
            DisplayStringItem that = (DisplayStringItem) o;
            return Objects.equals(value, that.value) && Objects.equals(params, that.params);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, params);
    }
}
