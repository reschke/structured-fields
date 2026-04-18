package org.greenbytes.http.sfv;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a Decimal.
 * <p>
 * A Decimal - despite it's name - is essentially the same thing as an Integer,
 * but has an implied divisor of 1000 (in other words, a scale of 3). Thus, a
 * value represented as {@code 0.5} in a field value will be internally stored
 * as {@code long} with value {@code 500}. The only difference to
 * {@link IntegerItem} is that {@link #get()} will return a {@link BigDecimal},
 * and that the implied divisor is taken into account when serializing the
 * value. {@link #getAsLong()} provides access to the raw value when the
 * overhead of {@link BigDecimal} is not needed.
 * 
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#decimal">Section
 *      3.3.2 of RFC 9651</a>
 */
public class DecimalItem implements NumberItem<BigDecimal> {

    private final long value;
    private final Parameters params;

    private static final long MIN = -999999999999999L;
    private static final long MAX = 999999999999999L;
    private static final BigDecimal THOUSAND = new BigDecimal(1000);

    private DecimalItem(long value, Parameters params) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("value must be in the range from " + MIN + " to " + MAX);
        }
        this.value = value;
        this.params = Objects.requireNonNull(params, "params must not be null");
    }

    /**
     * Creates a {@link DecimalItem} instance representing the specified
     * {@code long} value, where the implied divisor is {@code 1000}.
     * 
     * @param value
     *            a {@code long} value.
     * @return a {@link DecimalItem} representing {@code value}.
     */
    public static DecimalItem valueOf(long value) {
        return new DecimalItem(value, Parameters.EMPTY);
    }

    /**
     * Creates a {@link DecimalItem} instance representing the specified
     * {@code BigDecimal} value, with potential rounding.
     * 
     * @param value
     *            a {@code BigDecimal} value.
     * @return a {@link DecimalItem} representing {@code value}.
     */
    public static DecimalItem valueOf(BigDecimal value) {
        BigDecimal permille = (Objects.requireNonNull(value, "value must not be null")).multiply(THOUSAND);
        return valueOf(permille.longValue());
    }

    @Override
    public DecimalItem withParams(Parameters params) {
        return new DecimalItem(this.value, Objects.requireNonNull(params, "params must not be null"));
    }

    @Override
    public Parameters getParams() {
        return params;
    }

    public StringBuilder serializeToNoParams(StringBuilder sb) {

        String sign = value < 0 ? "-" : "";

        long abs = Math.abs(value);
        long left = abs / 1000;
        long right = abs % 1000;

        sb.append(sign).append(left).append('.');

        // first digit
        sb.append(right / 100);

        // second and third digit, except trailing zeroesfi
        if (right % 100 != 0) {
            sb.append((right / 10) % 10);
            if (right % 10 != 0) {
                sb.append(right % 10);
            }
        }

        return sb;
    }

    @Override
    public StringBuilder serializeTo(StringBuilder sb) {
        return params.serializeTo(serializeToNoParams(sb));
    }

    @Override
    public String serialize() {
        return serializeTo(new StringBuilder(20)).toString();
    }

    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel, Function<Class, String> formatter) {
        String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
        String classn = formatter.apply(this.getClass());
        sb = sb.append(indent);
        sb = serializeToNoParams(sb);
        sb = sb.append(classn).append("\n");
        return params.serializeToForDebug(sb, indentLevel + 2, formatter);
    }

    @Override
    public BigDecimal get() {
        return BigDecimal.valueOf(value, 3);
    }

    @Override
    public long getAsLong() {
        return value;
    }

    @Override
    public int getDivisor() {
        return 1000;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DecimalItem)) {
            return false;
        } else {
            DecimalItem that = (DecimalItem) o;
            return value == that.value && Objects.equals(params, that.params);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, params);
    }
}
