package org.greenbytes.http.sfv;

import java.util.List;
import java.util.Objects;

public class InnerListItem extends ListItem {

    private final Parameters params;

    private InnerListItem(List<Item<? extends Object>> value, Parameters params) {
        super(value);
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    public static InnerListItem valueOf(List<Item<? extends Object>> value) {
        return new InnerListItem(value, Parameters.EMPTY);
    }

    @Override
    public InnerListItem withParams(Parameters params) {
        if (Objects.requireNonNull(params, "params must not be null").get().isEmpty()) {
            return this;
        } else {
            return new InnerListItem(this.value, params);
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

}
