package org.greenbytes.http.sfv;

/**
 * Marker interface for Items.
 * 
 * @param <T>
 *            represented Java type
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#item">Section 3.3
 *      of RFC 9651</a>
 */
public interface Item<T> extends ListElement<T>, Parameterizable<T> {

    Item<T> withParams(Parameters params);
}
