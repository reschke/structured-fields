package org.greenbytes.http.sfv;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents the Parameters of an Item or an Inner List.
 * 
 * @see <a href=
 *      "https://greenbytes.de/tech/webdav/draft-ietf-httpbis-header-structure-18.html#param">Section
 *      3.1.2 of draft-ietf-httpbis-header-structure-18</a>
 */
public class Parameters implements Map<String, Item<? extends Object>> {

    private final Map<String, Item<? extends Object>> delegate;

    public static final Parameters EMPTY = new Parameters(Collections.emptyMap());

    private Parameters(Map<String, Item<? extends Object>> value) {
        this.delegate = Collections.unmodifiableMap(checkMap(value));
    }

    /**
     * Creates a {@link Parameters} instance representing the specified
     * {@code Map<String, Item>} value.
     * <p>
     * Note that the {@link Map} implementation that is used here needs to
     * iterate predictably based on insertion order, such as
     * {@link java.util.LinkedHashMap}.
     * 
     * @param value
     *            a {@code Map<String, Item>} value
     * @return a {@link Parameters} representing {@code value}.
     */
    public static Parameters valueOf(Map<String, Item<? extends Object>> value) {
        return new Parameters(value);
    }

    public StringBuilder serializeTo(StringBuilder sb) {
        for (Map.Entry<String, Item<? extends Object>> e : delegate.entrySet()) {
            sb.append(';').append(e.getKey());
            if (!(e.getValue().get().equals(Boolean.TRUE))) {
                sb.append('=');
                e.getValue().serializeTo(sb);
            }
        }
        return sb;
    }

    private static Map<String, Item<? extends Object>> checkMap(Map<String, Item<? extends Object>> map) {
        for (Map.Entry<String, Item<? extends Object>> entry : Objects.requireNonNull(map, "Map must not be null").entrySet()) {
            Utils.checkKey(entry.getKey());
            Item<? extends Object> value = entry.getValue();
            if (!value.getParams().isEmpty()) {
                throw new IllegalArgumentException("Parameter value must be bare item (no parameters)");
            }
            if (value instanceof InnerList) {
                throw new IllegalArgumentException("Parameter value cannot be an Inner List");
            }
        }
        return map;
    }

    // delegate methods, autogenerated

    public void clear() {
        delegate.clear();
    }

    public Item<? extends Object> compute(String key,
            BiFunction<? super String, ? super Item<? extends Object>, ? extends Item<? extends Object>> remappingFunction) {
        return delegate.compute(key, remappingFunction);
    }

    public Item<? extends Object> computeIfAbsent(String key,
            Function<? super String, ? extends Item<? extends Object>> mappingFunction) {
        return delegate.computeIfAbsent(key, mappingFunction);
    }

    public Item<? extends Object> computeIfPresent(String key,
            BiFunction<? super String, ? super Item<? extends Object>, ? extends Item<? extends Object>> remappingFunction) {
        return delegate.computeIfPresent(key, remappingFunction);
    }

    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    public Set<Entry<String, Item<? extends Object>>> entrySet() {
        return delegate.entrySet();
    }

    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    public void forEach(BiConsumer<? super String, ? super Item<? extends Object>> action) {
        delegate.forEach(action);
    }

    public Item<? extends Object> get(Object key) {
        return delegate.get(key);
    }

    public Item<? extends Object> getOrDefault(Object key, Item<? extends Object> defaultValue) {
        return delegate.getOrDefault(key, defaultValue);
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public Set<String> keySet() {
        return delegate.keySet();
    }

    public Item<? extends Object> merge(String key, Item<? extends Object> value,
            BiFunction<? super Item<? extends Object>, ? super Item<? extends Object>, ? extends Item<? extends Object>> remappingFunction) {
        return delegate.merge(key, value, remappingFunction);
    }

    public Item<? extends Object> put(String key, Item<? extends Object> value) {
        return delegate.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends Item<? extends Object>> m) {
        delegate.putAll(m);
    }

    public Item<? extends Object> putIfAbsent(String key, Item<? extends Object> value) {
        return delegate.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return delegate.remove(key, value);
    }

    public Item<? extends Object> remove(Object key) {
        return delegate.remove(key);
    }

    public boolean replace(String key, Item<? extends Object> oldValue, Item<? extends Object> newValue) {
        return delegate.replace(key, oldValue, newValue);
    }

    public Item<? extends Object> replace(String key, Item<? extends Object> value) {
        return delegate.replace(key, value);
    }

    public void replaceAll(BiFunction<? super String, ? super Item<? extends Object>, ? extends Item<? extends Object>> function) {
        delegate.replaceAll(function);
    }

    public int size() {
        return delegate.size();
    }

    public Collection<Item<? extends Object>> values() {
        return delegate.values();
    }

    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }
}
