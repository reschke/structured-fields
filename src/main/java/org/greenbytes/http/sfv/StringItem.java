package org.greenbytes.http.sfv;

import java.util.Objects;

public class StringItem implements Item<String> {

    private final String value;
    private final Parameters params;

    public StringItem(String value, Parameters params) {
        this.value = checkParam(Objects.requireNonNull(value, "value must not be null"));
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    public static StringItem valueOf(String value) {
        return new StringItem(value, Parameters.EMPTY);
    }

    @Override
    public StringItem withParams(Parameters params) {
        if (Objects.requireNonNull(params, "params must not be null").get().isEmpty()) {
            return this;
        } else {
            return new StringItem(this.value, params);
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

    private static String checkParam(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c < 0x20 || c >= 0x7f) {
                throw new IllegalArgumentException(String.format("Invalid character in String at position %d: '%c' (0x%04x)", i, c, (int)c));
            }
        }
        return value;
    }
}
