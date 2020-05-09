package org.greenbytes.http.sfv;

public class BooleanItem implements Item<Boolean> {

    private final boolean value;

    private static final BooleanItem TRUE = new BooleanItem(true);
    private static final BooleanItem FALSE = new BooleanItem(false);

    private BooleanItem(boolean value) {
        this.value = value;
    }

    public static BooleanItem valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        sb.append(serialize());
        return sb;
    }

    @Override
    public String serialize() {
        return value ? "?1" : "?0";
    }

    @Override
    public Boolean get() {
        return value;
    }
}
