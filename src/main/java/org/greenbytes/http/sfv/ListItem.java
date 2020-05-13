package org.greenbytes.http.sfv;

import java.util.List;

public class ListItem implements Item<List<Item<? extends Object>>> {

    private final boolean isInner;
    private final List<Item<? extends Object>> value;
    private final Parameters params;

    public ListItem(boolean isInner, List<Item<? extends Object>> value, Parameters params) {
        this.isInner = isInner;
        this.value = value;
        this.params = params;
        if (!isInner && !(params.getMap().isEmpty())) {
            throw new IllegalArgumentException("only inner lists can have parameters");
        }
    }

    public ListItem(boolean isInner, List<Item<? extends Object>> value) {
        this(isInner, value, Parameters.EMPTY);
    }

    @Override
    public ListItem withParams(Parameters params) {
        if (params.get().isEmpty()) {
            return this;
        } else {
            return new ListItem(this.isInner, this.value, params);
        }
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        String separator = "";

        if (isInner) {
            sb.append('(');
        }

        for (Item<? extends Object> i : value) {
            sb.append(separator);
            separator = isInner ? " " :", ";
            i.serializeTo(sb);
        }

        if (isInner) {
            sb.append(')');
        }

        params.serializeTo(sb);

        return sb;
    }


    @Override
    public Parameters getParams() {
        return params;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    @Override
    public List<Item<? extends Object>> get() {
        return value;
    }
}
