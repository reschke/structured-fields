package org.greenbytes.http.sfv;

/**
 * Marker interface for Items.
 * 
 * @param <T>
 *            represented Java type
 * @see <a href=
 *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#item">Section
 *      3.3 of draft-ietf-httpbis-header-structure-18</a>
 */
public interface Item<T> extends Parametrizable<T> {
}
