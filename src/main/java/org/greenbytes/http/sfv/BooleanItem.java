package org.greenbytes.http.sfv;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a Boolean.
 * 
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#boolean">Section
 *      3.3.6 of RFC 9651</a>
 */
public class BooleanItem implements Item<Boolean> {

    private final boolean value;
    private final Parameters params;

    private static final BooleanItem TRUE = new BooleanItem(true, Parameters.EMPTY);
    private static final BooleanItem FALSE = new BooleanItem(false, Parameters.EMPTY);

    private BooleanItem(boolean value, Parameters params) {
        this.value = value;
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    /**
     * @deprecated - use {#{@linkplain #of(boolean)} instead.
     */
    @Deprecated
    public static BooleanItem valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }

    /**
     * Creates a {@link BooleanItem} instance representing the specified
     * {@code boolean} value.
     *
     * @param value
     *            a {@code boolean} value.
     * @return a {@link BooleanItem} representing {@code value}.
     */
    public static BooleanItem of(boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    @Override
    public BooleanItem withParams(Parameters params) {
        return new BooleanItem(this.value, Objects.requireNonNull(params, "params must not be null"));
    }

    @Override
    public BooleanItem withParamValuesOf(Object... obs) {
        return new BooleanItem(this.value, Parameters.valueOf(obs));
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        sb.append(value ? "?1" : "?0");
        params.serializeTo(sb);
        return sb;
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    @Override
    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel, Function<Class, String> formatter) {
        String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
        String classn = formatter.apply(this.getClass());

        sb.append(indent).append(value ? "?1" : "?0").append(classn).append("\n");
        sb = params.serializeToForDebug(sb, indentLevel + 2, formatter);
        return sb;
    }

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BooleanItem)) {
            return false;
        } else {
            BooleanItem that = (BooleanItem) o;
            return value == that.value && Objects.equals(params, that.params);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, params);
    }
}
