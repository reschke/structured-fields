package org.greenbytes.http.sfv;

public class StringItem implements Item<String> {

    private final String value;

    public StringItem(String value) {
        this.value = value;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        sb.append('"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' || c == '"') {
                sb.append('\\');
            }
            sb.append(c);
        }
        sb.append('"');
        return sb;
    }

    @Override
    public String serialize() {
        return appendTo(new StringBuilder(2 + value.length())).toString();
    }

    @Override
    public String get() {
        return this.value;
    }
}
