package org.greenbytes.http.sfv;

import java.util.function.Supplier;

/**
 * Base interface for Structured Data Types.
 * <p>
 * Each type is a wrapper around the Java type it represents and which can be
 * retrieved using {@link Supplier#get()}.
 * 
 * @param <T>
 *            represented Java type
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc8941.html#">Section 3 of RFC
 *      8941</a>
 */
public interface Type<T> extends Supplier<T> {

    /**
     * Serialize to an existing {@link StringBuilder}.
     * 
     * @param sb
     *            where to serialize to
     * @return the {@link StringBuilder} so calls can be chained.
     */
    public StringBuilder serializeTo(StringBuilder sb);

    /**
     * Serialize.
     * 
     * @return the serialization.
     */
    public String serialize();
}
