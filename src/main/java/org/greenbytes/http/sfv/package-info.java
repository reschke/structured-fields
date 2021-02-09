/**
 * Implementation of <a href= "https://www.rfc-editor.org/rfc/rfc8941.html">IETF
 * RFC 8941: Structured Field Values for HTTP</a>.
 * <p>
 * Includes a {@link Parser} and object equivalents of the defined data types
 * (see {@link Type}).
 * <p>
 * Here's a minimal example:
 * 
 * <pre>
 * {
 *     &#64;code
 *     Parser p = new Parser("a=?0, b, c; foo=bar");
 *     Dictionary d = p.parseDictionary();
 *     for (Map.Entry<String, Item<? extends Object>> e : d.get()) {
 *         String key = e.getKey();
 *         Item<? extends Object> item = e.getValue();
 *         Object value = item.get();
 *         Parameters params = item.getParams();
 *         System.out.println(key + " -> " + value + (params.isEmpty() ? "" : (" (" + params.serialize() + ")")));
 *     }
 * }
 * </pre>
 * <p>
 * gives:
 * 
 * <pre>
 * {@code
 * a -> false
 * b -> true
 * c -> true (;foo=bar)}
 * </pre>
 */

package org.greenbytes.http.sfv;
