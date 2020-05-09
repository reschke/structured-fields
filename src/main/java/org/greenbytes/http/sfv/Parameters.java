package org.greenbytes.http.sfv;

import java.util.LinkedHashMap;
import java.util.Map;

public class Parameters implements Item<Map<String, Item<? extends Object>>> {

    private final LinkedHashMap<String, Item<? extends Object>> value;

    public Parameters(LinkedHashMap<String, Item<? extends Object>> value) {
        this.value = value;
    }

    @Override
    public Map<String, Item<? extends Object>> get() {
        return this.value;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        for (Map.Entry<String, Item<? extends Object>> e : this.value.entrySet()) {
            sb.append(';').append(e.getKey());
            if (!(e.getValue().get().equals(Boolean.TRUE))) {
                sb.append('=');
                e.getValue().appendTo(sb);
            }
        }
        return sb;
    }

    @Override
    public String serialize() {
        return appendTo(new StringBuilder()).toString();
    }

}
