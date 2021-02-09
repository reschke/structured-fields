package org.greenbytes.http.sfv;

/**
 * Marker interface for things that can be elements of Outer Lists.
 * 
 * @param <T>
 *            represented Java type
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc8941.html#item">Section 3.3
 *      of RFC 8941</a>
 */
public interface ListElement<T> extends Parametrizable<T> {
}
