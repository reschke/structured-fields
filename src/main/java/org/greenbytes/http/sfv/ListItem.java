package org.greenbytes.http.sfv;

import java.util.List;

public class ListItem implements Item<List<Item<? extends Object>>> {

    private final List<Item<? extends Object>> value;

    public ListItem(List<Item<? extends Object>> value) {
        this.value = value;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        String separator = "";

        for (Item<? extends Object> i : value) {
            sb.append(separator);
            separator = ", ";
            i.appendTo(sb);
        }

        return sb;
    }

    @Override
    public String serialize() {
        return appendTo(new StringBuilder()).toString();
    }

    @Override
    public List<Item<? extends Object>> get() {
        return value;
    }
}
