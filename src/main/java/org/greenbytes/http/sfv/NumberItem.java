package org.greenbytes.http.sfv;

import java.util.function.LongSupplier;

/**
 * Common interface for all {@link Type}s that can carry numbers.
 * 
 * @param <T>
 *            represented Java type
 * @see <a href=
 *      "https://www.rfc-editor.org/rfc/rfc8941.html#integer">Section
 *      3.3.1 of RFC 8941</a>
 * @see <a href=
 *      "https://www.rfc-editor.org/rfc/rfc8941.html#decimal">Section
 *      3.3.2 of RFC 8941</a>
 */
public interface NumberItem<T> extends Item<T>, LongSupplier {

    /**
     * Returns the divisor to be used to obtain the actual numerical value (as
     * opposed to the underlying long value returned by
     * {@link LongSupplier#getAsLong()}).
     * 
     * @return the divisor ({@code 1} for Integers, {@code 1000} for Decimals)
     */
    int getDivisor();

    NumberItem<T> withParams(Parameters params);
}
