package org.greenbytes.http.sfv;

public class BooleanItem implements Item<Boolean> {

    private final boolean value;


    public BooleanItem(boolean value) {
        this.value = value;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        sb.append(value ? "?1" : "?0");
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
