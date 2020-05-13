package org.greenbytes.http.sfv;

import java.math.BigDecimal;

public class DecimalItem implements Item<BigDecimal> {

    private final long value;
    private final Parameters params;

    private static final long MIN = -999999999999999L;
    private static final long MAX = 999999999999999L;

    public DecimalItem(long value, Parameters params) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("value must be in the range from " + MIN + " to " + MAX);
        }
        this.value = value;
        this.params = params;
    }

    public DecimalItem(long value) {
        this(value, Parameters.EMPTY);
    }

    @Override
    public DecimalItem withParams(Parameters params) {
        if (params.get().isEmpty()) {
            return this;
        } else {
            return new DecimalItem(this.value, params);
        }
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
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

        params.serializeTo(sb);

        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder(20)).toString();
    }

    @Override
    public BigDecimal get() {
        return BigDecimal.valueOf(this.value, 3);
    }
}
