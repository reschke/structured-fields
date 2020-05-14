package org.greenbytes.http.sfv;

/**
 * Common interface for all {@link Type}s that can carry {@link Parameters}.
 * 
 * @param <T>
 *            represented Java type
 * @see <a href=
 *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#item">Section
 *      3.3 of draft-ietf-httpbis-header-structure-18</a>
 */
public interface Item<T> extends Type<T> {

    /**
     * Given an existing {@link Item}, return a new instance with the specified
     * {@link Parameters}.
     * 
     * @param params
     *            {@link Parameters} to set (must be non-null)
     * @return new instance with specified {@link Parameters}.
     */
    public Item<T> withParams(Parameters params);

    /**
     * Get the {@link Parameters} of this {@link Item}.
     * 
     * @return the parameters.
     */
    public Parameters getParams();
}
