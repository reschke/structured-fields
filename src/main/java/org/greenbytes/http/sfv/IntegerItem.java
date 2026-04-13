package org.greenbytes.http.sfv;

import java.util.Objects;

/**
 * Represents an Integer.
 * 
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#integer">Section
 *      3.3.1 of RFC 9651</a>
 */
public class IntegerItem implements NumberItem<Long> {

    private final long value;
    private final Parameters params;

    private static final long MIN = -999999999999999L;
    private static final long MAX = 999999999999999L;

    private IntegerItem(long value, Parameters params) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("value must be in the range from " + MIN + " to " + MAX);
        }
        this.value = value;
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    /**
     * Creates an {@link IntegerItem} instance representing the specified
     * {@code long} value.
     * 
     * @param value
     *            a {@code long} value.
     * @return a {@link IntegerItem} representing {@code value}.
     */
    public static IntegerItem valueOf(long value) {
        return new IntegerItem(value, Parameters.EMPTY);
    }

    @Override
    public IntegerItem withParams(Parameters params) {
        return new IntegerItem(this.value, Objects.requireNonNull(params, "params must not be null"));
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        sb.append(value);
        params.serializeTo(sb);
        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    @Override
    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel) {
        String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
        String classn = " (" + this.getClass().getSimpleName() + ")";
        return sb.append(indent).append(value).append(classn).append("\n")
                .append(params.serializeToForDebug(new StringBuilder(), indentLevel + 2)).append("\n");
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
        if (!(o instanceof IntegerItem)) {
            return false;
        } else {
            IntegerItem that = (IntegerItem) o;
            return value == that.value && Objects.equals(params, that.params);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, params);
    }
}
