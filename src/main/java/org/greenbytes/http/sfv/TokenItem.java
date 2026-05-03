package org.greenbytes.http.sfv;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a Token.
 * 
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#token">Section
 *      3.3.4 of RFC 9651</a>
 */
public class TokenItem implements Item<String> {

    private final String value;
    private final Parameters params;

    private TokenItem(String value, Parameters params) {
        this.value = checkParam(Objects.requireNonNull(value, "value must not be null"));
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    /**
     * Creates a {@link TokenItem} instance representing the specified
     * {@code String} value.
     *
     * @param value
     *            a {@code String} value.
     * @return a {@link TokenItem} representing {@code value}.
     */
    public static TokenItem of(String value) {
        return new TokenItem(value, Parameters.EMPTY);
    }

    @Override
    public TokenItem withParams(Parameters params) {
        return new TokenItem(this.value, Objects.requireNonNull(params, "params must not be null"));
    }

    @Override
    public TokenItem withParamValuesOf(Object... obs) {
        return new TokenItem(this.value, Parameters.valueOf(obs));
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

    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel, Function<Class, String> formatter) {
        String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
        String classn = formatter.apply(this.getClass());

        sb = sb.append(indent).append(value).append(classn).append("\n");
        sb = params.serializeToForDebug(sb, indentLevel + 2, formatter);
        return sb;
    }

    @Override
    public String get() {
        return this.value;
    }

    private static String checkParam(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Token can not be empty");
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if ((i == 0 && (c != '*' && !Utils.isAlpha(c))) || (c <= ' ' || c >= 0x7f || "\"(),;<=>?@[\\]{}".indexOf(c) >= 0)) {
                throw new IllegalArgumentException(
                        String.format("Invalid character in Token at position %d: '%c' (0x%04x)", i, c, (int) c));
            }
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TokenItem)) {
            return false;
        } else {
            TokenItem tokenItem = (TokenItem) o;
            return Objects.equals(value, tokenItem.value) && Objects.equals(params, tokenItem.params);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, params);
    }
}
