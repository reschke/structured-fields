package org.greenbytes.http.sfv;

import java.util.Map;

public class Dictionary implements Type<Map<String, Item<? extends Object>>> {

    private final Map<String, Item<? extends Object>> value;

    public Dictionary(Map<String, Item<? extends Object>> value) {
        this.value = value;
    }

    public Map<String, Item<? extends Object>> get() {
        return value;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        String separator = "";

        for (Map.Entry<String, Item<? extends Object>> e : value.entrySet()) {
            sb.append(separator);
            separator = ", ";

            String name = e.getKey();
            Item<? extends Object> value = e.getValue();

            sb.append(name);
            if (Boolean.TRUE.equals(value.get())) {
                value.getParams().serializeTo(sb);
            } else {
                sb.append("=");
                value.serializeTo(sb);
            }
        }

        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }
}
