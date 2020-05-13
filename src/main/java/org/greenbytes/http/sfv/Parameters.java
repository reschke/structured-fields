package org.greenbytes.http.sfv;

import java.util.Collections;
import java.util.Map;

public class Parameters {

    private final Map<String, Item<? extends Object>> value;

    public static final Parameters EMPTY = new Parameters(Collections.emptyMap());

    public Parameters(Map<String, Item<? extends Object>> value) {
        this.value = value;
    }

    public Map<String, Item<? extends Object>> get() {
        return value;
    }

    public Map<String, Item<? extends Object>> getMap() {
        return value;
    }

    public StringBuilder serializeTo(StringBuilder sb) {
        for (Map.Entry<String, Item<? extends Object>> e : value.entrySet()) {
            sb.append(';').append(e.getKey());
            if (!(e.getValue().get().equals(Boolean.TRUE))) {
                sb.append('=');
                e.getValue().serializeTo(sb);
            }
        }
        return sb;
    }

    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }
}
