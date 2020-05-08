package org.greenbytes.http.sfv;

public class DecimalItem implements Item {

    private final long value;

    private static final long MIN = -999999999999999L;
    private static final long MAX = 999999999999999L;

    public DecimalItem(long value) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("value must be in the range from " + MIN + " to " + MAX);
        }
        this.value = value;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        String sign = value < 0 ? "-" : "";
        long abs = Math.abs(value);
        long left = abs / 1000;
        long right = abs % 1000;

        if (right % 10 == 0) {
            right /= 10;
        }
        if (right % 10 == 0) {
            right /= 10;
        }

        sb.append(sign).append(Long.toString(left)).append('.').append(Long.toString(right));

        return sb;
    }

    @Override
    public String serialize() {
        return appendTo(new StringBuilder(20)).toString();
    }
}
