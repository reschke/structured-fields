package org.greenbytes.http.sfv;

import java.util.List;
import java.util.Objects;

public class OuterList implements Type<List<Item<? extends Object>>> {

    private final List<Item<? extends Object>> value;

    private OuterList(List<Item<? extends Object>> value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    public static OuterList valueOf(List<Item<? extends Object>> value) {
        return new OuterList(value);
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
