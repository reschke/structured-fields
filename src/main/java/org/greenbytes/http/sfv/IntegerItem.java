package org.greenbytes.http.sfv;

import java.util.function.LongSupplier;

public class IntegerItem implements Item<Long>, LongSupplier {

    private final long value;
    private final Parameters params;

    private static final long MIN = -999999999999999L;
    private static final long MAX = 999999999999999L;

    private IntegerItem(long value, Parameters params) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("value must be in the range from " + MIN + " to " + MAX);
        }
        this.value = value;
        this.params = params;
    }

    public static IntegerItem valueOf(long value) {
        return new IntegerItem(value, Parameters.EMPTY);
    }

    @Override
    public IntegerItem withParams(Parameters params) {
        if (params.get().isEmpty()) {
            return this;
        } else {
            return new IntegerItem(this.value, params);
        }
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        sb.append(Long.toString(value));
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
}
