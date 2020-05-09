package org.greenbytes.http.sfv;

import java.util.LinkedHashMap;
import java.util.Map;

public class Parameters {

    private final LinkedHashMap<String, Item<? extends Object>> value;

    public Parameters(LinkedHashMap<String, Item<? extends Object>> value) {
        this.value = value;
    }

    public Map<String, Item<? extends Object>> get() {
        return this.value;
    }

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

    public String serialize() {
        return appendTo(new StringBuilder()).toString();
    }

}
