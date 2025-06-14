package org.greenbytes.http.sfv;

/**
 * Common interface for all {@link Type}s that can carry {@link Parameters}.
 * 
 * @param <T>
 *            represented Java type
 * @see <a href= "https://www.rfc-editor.org/rfc/rfc9651.html#param">Section
 *      3.1.2 of RFC 9651</a>
 */
public interface Parameterizable<T> extends Type<T> {

    /**
     * Given an existing {@link Item}, return a new instance with the specified
     * {@link Parameters}.
     * 
     * @param params
     *            {@link Parameters} to set (must be non-null)
     * @return new instance with specified {@link Parameters}.
     */
    Parameterizable<T> withParams(Parameters params);

    /**
     * Get the {@link Parameters} of this {@link Item}.
     * 
     * @return the parameters.
     */
    Parameters getParams();
}
