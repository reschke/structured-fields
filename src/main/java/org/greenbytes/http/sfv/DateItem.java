package org.greenbytes.http.sfv;

import java.util.Objects;

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
    public Parameters getParams() {
        return params;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        sb.append('@');
        sb.append(value);
        params.serializeTo(sb);
        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
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
