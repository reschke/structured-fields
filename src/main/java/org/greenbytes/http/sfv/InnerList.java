package org.greenbytes.http.sfv;

import java.util.List;
import java.util.Objects;

public class InnerList implements Item<List<Item<? extends Object>>> {

    private final List<Item<? extends Object>> value;
    private final Parameters params;

    private InnerList(List<Item<? extends Object>> value, Parameters params) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    public static InnerList valueOf(List<Item<? extends Object>> value) {
        return new InnerList(value, Parameters.EMPTY);
    }

    @Override
    public InnerList withParams(Parameters params) {
        if (Objects.requireNonNull(params, "params must not be null").get().isEmpty()) {
            return this;
        } else {
            return new InnerList(this.value, params);
        }
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        String separator = "";

        sb.append('(');

        for (Item<? extends Object> i : value) {
            sb.append(separator);
            separator = " ";
            i.serializeTo(sb);
        }

        sb.append(')');

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