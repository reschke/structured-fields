package org.greenbytes.http.sfv;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Common  {@linkplain Item} methods.
 * 
 * @param <T>
 *            represented Java type
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#item">Section 3.3
 *      of RFC 9651</a>
 */
public interface Item<T> extends ListElement<T>, Parameterizable<T> {

    Item<T> withParams(Parameters params);

    /**
     * @see #parse(String...)
     */
    static Item<?> parse(String input) {
        return new Parser(input).parseItem();
    }

    /**
     * @see #parse(String...)
     */
    static Item<?> parse(List<String> input) {
        return new Parser(input).parseItem();
    }

    /**
     * Implementation of "Parsing an Item"
     *
     * @param input values of file values.
     * @return result of parse as {@link Item}.
     *
     * @see <a href=
     *      "https://www.rfc-editor.org/rfc/rfc9651.html#parse-item">Section
     *      4.2.3 of RFC 9651</a>
     */
    static Item<?> parse(String... input) {
        return new Parser(input).parseItem();
    }

    /**
     * @return the ByteBuffer value of this item if and only if of type {@linkplain SfDataType#BOOLEAN}
     * @throws UnsupportedOperationException otherwise
     */
    default boolean booleanValue() {
        throw new UnsupportedOperationException("not a Boolean Item, type is: " + getType());
    }

    /**
     * @return the ByteBuffer value of this item if and only if of type {@linkplain SfDataType#BYTESEQUENCE}
     * @throws UnsupportedOperationException otherwise
     */
    default ByteBuffer byteBufferValue() {
        throw new UnsupportedOperationException("not a Byte Sequence Item, type is: " + getType());
    }

    /**
     * @return the double value of this item if and only if of type {@linkplain SfDataType#DECIMAL}
     * @throws UnsupportedOperationException otherwise
     */
    default double doubleValue() {
        throw new UnsupportedOperationException("not a Decimal Item, type is: " + getType());
    }

    /**
     * @return the long value of this item if and only if of type {@linkplain SfDataType#INTEGER}
     * @throws UnsupportedOperationException otherwise
     */
    default long longValue() {
        throw new UnsupportedOperationException("not an Integer Item, type is: " + getType());
    }

    /**
     * @return the String  value of this item if and only if of type {@linkplain SfDataType#STRING}
     * @throws UnsupportedOperationException otherwise
     */
    default String stringValue() {
        throw new UnsupportedOperationException("not a String Item, type is: " + getType());
    }

    /**
     * @return the String value of this item if and only if of type {@linkplain SfDataType#TOKEN}
     * @throws UnsupportedOperationException otherwise
     */
    default String tokenValue() {
        throw new UnsupportedOperationException("not a Token Item, type is: " + getType());
    }
}
