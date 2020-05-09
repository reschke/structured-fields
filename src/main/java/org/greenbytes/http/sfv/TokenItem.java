package org.greenbytes.http.sfv;

public class TokenItem implements Item<String> {

    private final String value;

    public TokenItem(String value) {
        this.value = value;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        sb.append(this.value);
        return sb;
    }

    @Override
    public String serialize() {
        return this.value;
    }

    @Override
    public String get() {
        return this.value;
    }
}
