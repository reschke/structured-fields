/**
 * Implementation of IETF draft <a href=
 * "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html">Structured
 * Field Values for HTTP</a>.
 * <p>
 * Includes a {@link Parser} and object equivalents of the defined data types
 * (see {@link Type}).
 * <p>
 * Here's a minimal example:
 * <pre>{@code
 *   Parser p = new Parser("a=?0, b, c; foo=bar");
 *   Dictionary d = p.parseDictionary();
 *   for (Map.Entry<String,Item<? extends Object>> e : d.get()) {
 *     String key = e.getKey();
 *     Item<? extends Object> item = e.getValue();
 *     Object value = item.get();
 *     Parameters params = item.getParams();
 *     System.out.println(key + " -> " + value + (params.get().isEmpty() ? "" : (" (" + params.serialize() + ")")));
 *   }
 * }</pre>
 * <p>
 * gives:
 * <pre>{@code
 * a -&gt; false
 * b -&gt; true
 * c -&gt; true (;foo=bar)}</pre>
 */

package org.greenbytes.http.sfv;
