package org.greenbytes.http.sfv;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a Date.
 *
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#date">Section
 *      3.3.7 of RFC 9651</a>
 */
public class DateItem implements NumberItem<Long> {

    private final long value;
    private final Parameters params;

    private static final long MIN = -999999999999999L;
    private static final long MAX = 999999999999999L;

    private DateItem(long value, Parameters params) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("value must be in the range from " + MIN + " to " + MAX);
        }
        this.value = value;
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    @Override
    public SfDataType getType() {
        return SfDataType.DATE;
    }

    /**
     * Creates an {@link DateItem} instance representing the specified
     * {@code long} value.
     * 
     * @param value
     *            a {@code long} value.
     * @return a {@link DateItem} representing {@code value}.
     */
    public static DateItem valueOf(long value) {
        return new DateItem(value, Parameters.EMPTY);
    }

    @Override
    public DateItem withParams(Parameters params) {
        return new DateItem(this.value, Objects.requireNonNull(params, "params must not be null"));
    }

    @Override
    public DateItem withParamValuesOf(Object... obs) {
        return new DateItem(this.value, Parameters.valueOf(obs));
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    private StringBuilder serializeToNoParams(StringBuilder sb) {
        return sb.append('@').append(value);
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        return params.serializeTo(serializeToNoParams(sb));
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
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
    public Long get() {
        return value;
    }

    @Override
    public long getAsLong() {
        return value;
    }

    @Override
    public int getDivisor() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DateItem)) {
            return false;
        } else {
            DateItem dateItem = (DateItem) o;
            return value == dateItem.value && Objects.equals(params, dateItem.params);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, params);
    }
}
