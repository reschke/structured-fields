package org.greenbytes.http.sfv;

public class BooleanItem implements Item<Boolean> {

    private final boolean value;
    private final Parameters params;

    private static final BooleanItem TRUE = new BooleanItem(true);
    private static final BooleanItem FALSE = new BooleanItem(false);

    private BooleanItem(boolean value, Parameters params) {
        this.value = value;
        this.params = params;
    }

    private BooleanItem(boolean value) {
        this(value, null);
    }

    public static BooleanItem valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public BooleanItem withParams(Parameters params) {
        if (params.get().isEmpty()) {
            return this;
        } else {
            return new BooleanItem(this.value, params);
        }
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        sb.append(value ? "?1" : "?0");

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
    public Boolean get() {
        return value;
    }
}
