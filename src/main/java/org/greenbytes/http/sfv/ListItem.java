package org.greenbytes.http.sfv;

import java.util.List;

public class ListItem implements Item {

    private final List<Item> value;

    public ListItem(List<Item> value) {
        this.value = value;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        String separator = "";

        for (Item i : value) {
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
}
