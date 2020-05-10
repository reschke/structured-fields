package org.greenbytes.http.sfv;

public class IntegerItem implements Item<Long> {

    private final long value;
    private final Parameters params;

    private static final long MIN = -999999999999999L;
    private static final long MAX = 999999999999999L;

    public IntegerItem(long value, Parameters params) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("value must be in the range from " + MIN + " to " + MAX);
        }
        this.value = value;
        this.params = params;
    }

    public IntegerItem(long value) {
        this(value, null);
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

        if (params != null) {
            params.serializeTo(sb);
        }

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
}
