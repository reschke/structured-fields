package org.greenbytes.http.sfv;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class Parameters implements Supplier<Map<String, Item<? extends Object>>> {

    private final Map<String, Item<? extends Object>> value;

    public static final Parameters EMPTY = new Parameters(Collections.emptyMap());

    private Parameters(Map<String, Item<? extends Object>> value) {
        this.value = Collections.unmodifiableMap(Objects.requireNonNull(value, "value must not be null"));
    }

    public static Parameters valueOf(Map<String, Item<? extends Object>> value) {
        return new Parameters(value);
    }

    @Override
    public Map<String, Item<? extends Object>> get() {
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
