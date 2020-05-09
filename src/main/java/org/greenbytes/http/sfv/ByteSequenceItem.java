package org.greenbytes.http.sfv;

import java.nio.ByteBuffer;
import java.util.Base64;

public class ByteSequenceItem implements Item<ByteBuffer> {

    private final byte[] value;

    private static Base64.Encoder ENCODER = Base64.getEncoder();

    public ByteSequenceItem(byte[] value) {
        this.value = value;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        sb.append(':');
        sb.append(ENCODER.encodeToString(this.value));
        sb.append(':');
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
