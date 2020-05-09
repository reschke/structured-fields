package org.greenbytes.http.sfv;

public class StringItem implements Item<String> {

    private final String value;
    private final Parameters params;

    public StringItem(String value, Parameters params) {
        this.value = value;
        this.params = params;
    }

    public StringItem(String value) {
        this(value, null);
    }

    @Override
    public StringItem withParams(Parameters params) {
        if (params.get().isEmpty()) {
            return this;
        } else {
            return new StringItem(this.value, params);
        }
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

        if (params != null) {
            params.serializeTo(sb);
        }

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
