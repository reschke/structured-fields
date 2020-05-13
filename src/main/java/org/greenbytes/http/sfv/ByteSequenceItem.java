package org.greenbytes.http.sfv;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Objects;

public class ByteSequenceItem implements Item<ByteBuffer> {

    private final byte[] value;
    private final Parameters params;

    private static Base64.Encoder ENCODER = Base64.getEncoder();

    public ByteSequenceItem(byte[] value, Parameters params) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    public static ByteSequenceItem valueOf(byte[] value) {
        return new ByteSequenceItem(value, Parameters.EMPTY);
    }

    @Override
    public ByteSequenceItem withParams(Parameters params) {
        if (Objects.requireNonNull(params, "params must not be null").get().isEmpty()) {
            return this;
        } else {
            return new ByteSequenceItem(this.value, params);
        }
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        sb.append(':');
        sb.append(ENCODER.encodeToString(this.value));
        sb.append(':');
        params.serializeTo(sb);
        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    @Override
    public ByteBuffer get() {
        // TODO: this makes the value mutable; maybe duplicate?
        return ByteBuffer.wrap(this.value);
    }
}
