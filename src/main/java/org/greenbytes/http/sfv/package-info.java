/**
 * Implementation of <a href= "https://www.rfc-editor.org/rfc/rfc9651.html">IETF
 * RFC 9651: Structured Field Values for HTTP</a>.
 * <h2>Parsing</h2>
 * <p>
 * As a low-level
 * Includes a {@link org.greenbytes.http.sfv.Parser} and object equivalents of the defined data types
 * (see {@link org.greenbytes.http.sfv.Type}).
 * <p>
 * <h3>"Low Level" API</h3>
 * This represents the parsing results as direct Java equivalents of the RFC types.
 * Here's a minimal example:
 * 
 * <pre><code>
 * Dictionary d = Dictionary.parse("a=?0, b, c; foo=bar");
 * for (Map.Entry&lt;String, Item&lt;? extends Object&gt;&gt; e : d.get()) {
 *   String key = e.getKey();
 *   Item&lt;? extends Object&gt; item = e.getValue();
 *   Object value = item.get();
 *   Parameters params = item.getParams();
 *   System.out.println(key + " -&gt; " + value + (params.isEmpty() ? "" : (" (" + params.serialize() + ")")));
 * }
 * </code></pre>
 * <p>
 * gives:
 * 
 * <pre>
 * a -&gt; false
 * b -&gt; true
 * c -&gt; true (;foo=bar)
 * </pre>
 * <h3>Higher Level API</h3>
 * This API variant is easier to use when the types are known beforehand, and it is acceptable to
 * fail procesisng when they do not match the parsing result:
 *
 * <pre><code>
 * </code></pre>
 */

@Version("1.0.0")
package org.greenbytes.http.sfv;

import org.osgi.annotation.versioning.Version;