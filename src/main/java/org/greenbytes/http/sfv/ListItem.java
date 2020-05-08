package org.greenbytes.http.sfv;

import java.util.List;

public class ListItem implements Item {

    private final List<Item> value;

    public ListItem(List<Item> value) {
        this.value = value;
    }

    @Override
    public String serialize() {
        StringBuilder result = new StringBuilder();
        String separator = "";

        for (Item i : value) {
            result.append(separator);
            separator = ", ";
            result.append(i.serialize());
        }
        return result.toString();
    }
}
