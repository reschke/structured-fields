package org.greenbytes.http.sfv;

/**
 * Marker interface for Items.
 * 
 * @param <T>
 *            represented Java type
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc8941.html#item">Section 3.3
 *      of RFC 8941</a>
 */
public interface Item<T> extends ListElement<T>, Parametrizable<T> {

    Item<T> withParams(Parameters params);
}
