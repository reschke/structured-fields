package org.greenbytes.http.sfv;

import java.util.List;
import java.util.Objects;

public class ListItem implements Item<List<Item<? extends Object>>> {

    protected final List<Item<? extends Object>> value;

    protected ListItem(List<Item<? extends Object>> value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    public static ListItem valueOf(List<Item<? extends Object>> value) {
        return new ListItem(value);
    }

    @Override
    public ListItem withParams(Parameters params) {
        if (params.get().isEmpty()) {
            return this;
        } else {
            throw new IllegalArgumentException("only inner lists can have parameters");
        }
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        String separator = "";

        for (Item<? extends Object> i : value) {
            sb.append(separator);
            separator = ", ";
            i.serializeTo(sb);
        }

        return sb;
    }

    @Override
    public Parameters getParams() {
        throw new UnsupportedOperationException("only inner lists can have parameters");
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
