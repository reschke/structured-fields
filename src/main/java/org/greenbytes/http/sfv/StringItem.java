package org.greenbytes.http.sfv;

public class StringItem implements Item {

    private final String value;

    public StringItem(String value) {
        this.value = value;
    }

    @Override
    public String serialize() {
        StringBuilder result = new StringBuilder(2 + value.length());
        result.append('"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' || c == '"') {
                result.append('\\');
            }
            result.append(c);
        }
        result.append('"');
        return result.toString();
    }
}
