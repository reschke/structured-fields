package org.greenbytes.http.sfv;

public class TokenItem implements Item<String> {

    private final String value;
    private final Parameters params;

    public TokenItem(String value, Parameters params) {
        this.value = value;
        this.params = params;
    }

    public TokenItem(String value) {
        this(value, Parameters.EMPTY);
    }

    @Override
    public TokenItem withParams(Parameters params) {
        if (params.get().isEmpty()) {
            return this;
        } else {
            return new TokenItem(this.value, params);
        }
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        sb.append(this.value);
        params.serializeTo(sb);
        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    @Override
    public String get() {
        return this.value;
    }
}
