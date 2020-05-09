package org.greenbytes.http.sfv;

import java.nio.ByteBuffer;
import java.util.Base64;

public class ByteSequenceItem implements Item<ByteBuffer> {

    private final byte[] value;
    private final Parameters params;

    private static Base64.Encoder ENCODER = Base64.getEncoder();

    public ByteSequenceItem(byte[] value, Parameters params) {
        this.value = value;
        this.params= params;
    }

    public ByteSequenceItem(byte[] value) {
        this(value, null);
    }

    @Override
    public ByteSequenceItem withParams(Parameters params) {
        if (params.get().isEmpty()) {
            return this;
        } else {
            return new ByteSequenceItem(this.value, params);
        }
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        sb.append(':');
        sb.append(ENCODER.encodeToString(this.value));
        sb.append(':');

        if (params != null) {
            params.appendTo(sb);
        }

        return sb;
    }

    @Override
    public String serialize() {
        return appendTo(new StringBuilder()).toString();
    }

    @Override
    public ByteBuffer get() {
        return ByteBuffer.wrap(this.value);
    }
}
