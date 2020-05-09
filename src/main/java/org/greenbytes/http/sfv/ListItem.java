package org.greenbytes.http.sfv;

import java.util.List;

public class ListItem implements Item<List<Item<? extends Object>>> {

    private final List<Item<? extends Object>> value;

    public ListItem(List<Item<? extends Object>> value) {
        this.value = value;
    }

    @Override
    public Item<List<Item<? extends Object>>> withParams(Parameters params) {
        throw new UnsupportedOperationException();
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
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    @Override
    public List<Item<? extends Object>> get() {
        return value;
    }
}
