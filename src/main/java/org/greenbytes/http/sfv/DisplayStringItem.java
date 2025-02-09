package org.greenbytes.http.sfv;

import java.nio.charset.StandardCharsets;
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
        sb.append("%\"");
        byte[] octets = value.getBytes(StandardCharsets.UTF_8);
        for (byte b : octets) {
            if (b == 0x25 || b == 0x22 || b <= 0x1f || b == 0x7f) {
                sb.append('%');
                sb.append(Character.forDigit((b >> 4) & 0xf, 16));
                sb.append(Character.forDigit(b & 0xf, 16));
            } else {
                sb.append((char) b);
            }
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
