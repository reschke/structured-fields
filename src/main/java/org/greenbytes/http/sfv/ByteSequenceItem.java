package org.greenbytes.http.sfv;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a Byte Sequence.
 * 
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#binary">Section
 *      3.3.5 of RFC 9651</a>
 */
public class ByteSequenceItem implements Item<ByteBuffer> {

    private final byte[] value;
    private final Parameters params;

    private static final Base64.Encoder ENCODER = Base64.getEncoder();

    private ByteSequenceItem(byte[] value, Parameters params) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    @Override
    public SfDataType getType() {
        return SfDataType.BYTESEQUENCE;
    }

    /**
     * Creates a {@link ByteSequenceItem} instance representing the specified
     * {@code byte[]} value.
     * 
     * @param value
     *            a {@code byte[]} value.
     * @return a {@link ByteSequenceItem} representing {@code value}.
     */
    public static ByteSequenceItem valueOf(byte[] value) {
        return new ByteSequenceItem(value, Parameters.EMPTY);
    }

    @Override
    public ByteSequenceItem withParams(Parameters params) {
        return new ByteSequenceItem(this.value, Objects.requireNonNull(params, "params must not be null"));
    }

    @Override
    public ByteSequenceItem withParamValuesOf(Object... obs) {
        return new ByteSequenceItem(this.value, Parameters.valueOf(obs));
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    private StringBuilder serializeToNoParams(StringBuilder sb) {
        sb.append(':');
        sb.append(ENCODER.encodeToString(this.value));
        sb.append(':');
        return sb;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        return params.serializeTo(serializeToNoParams(sb));
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    @Override
    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel, Function<Class, String> formatter) {
        String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
        String classn = formatter.apply(this.getClass());

        sb = sb.append(indent);
        sb = serializeToNoParams(sb);
        sb.append(classn).append("\n");
        sb = params.serializeToForDebug(sb, indentLevel + 2, formatter);
        return sb;
    }


    @Override
    public ByteBuffer get() {
        // this returns a wrapper around a copy so that the object itself
        // stays immutable
        return ByteBuffer.wrap(this.value.clone());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ByteSequenceItem)) {
            return false;
        } else {
            ByteSequenceItem that = (ByteSequenceItem) o;
            return Objects.deepEquals(value, that.value) && Objects.equals(params, that.params);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(value), params);
    }
}
