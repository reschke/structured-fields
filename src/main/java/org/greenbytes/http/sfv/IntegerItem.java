package org.greenbytes.http.sfv;

public class IntegerItem implements Item<Long> {

    private final long value;

    private static final long MIN = -999999999999999L;
    private static final long MAX = 999999999999999L;

    public IntegerItem(long value) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("value must be in the range from " + MIN + " to " + MAX);
        }
        this.value = value;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        sb.append(Long.toString(value));
        return sb;
    }

    @Override
    public String serialize() {
        return Long.toString(value);
    }

    @Override
    public Long get() {
        return value;
    }
}
