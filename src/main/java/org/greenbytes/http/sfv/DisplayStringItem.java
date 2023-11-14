package org.greenbytes.http.sfv;

import java.util.Objects;

/**
 */
public class DisplayStringItem implements Item<String> {

    private final String value;
    private final Parameters params;

    private DisplayStringItem(String value, Parameters params) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    /**
     * Creates a {@link StringItem} instance representing the specified
     * {@code String} value.
     * 
     * @param value
     *            a {@code String} value.
     * @return a {@link StringItem} representing {@code value}.
     */
    public static DisplayStringItem valueOf(String value) {
        return new DisplayStringItem(value, Parameters.EMPTY);
    }

    @Override
    public DisplayStringItem withParams(Parameters params) {
        if (Objects.requireNonNull(params, "params must not be null").isEmpty()) {
            return this;
        } else {
            return new DisplayStringItem(this.value, params);
        }
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        sb.append('"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' || c == '"') {
                sb.append('\\');
            }
            sb.append(c);
        }
        sb.append('"');
        params.serializeTo(sb);
        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder(2 + value.length())).toString();
    }

    @Override
    public String get() {
        return this.value;
    }
}
