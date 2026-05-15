package org.greenbytes.http.sfv;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
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
 *      "https://www.rfc-editor.org/rfc/rfc9651.html#param">Section
 *      3.1.2 of RFC 9651</a>
 */
public class Parameters implements Map<String, Item<?>> {

    private final Map<String, Item<?>> delegate;

    /** Empty parameters instance. */
    protected static final Parameters EMPTY = new Parameters(Collections.emptyMap());

    private Parameters(Map<String, Object> value) {
        this.delegate = Collections.unmodifiableMap(checkAndTransformMap(value));
    }

    /**
     * Creates an unmodifiable {@link Parameters} instance representing the
     * specified {@code Map<String, Item>} value.
     * <p>
     * Note that the {@link Map} implementation that is used here needs to
     * iterate predictably based on insertion order, such as
     * {@link java.util.LinkedHashMap}.
     *
     * @param value
     *            a {@code Map<String, Item>} value
     * @return a {@link Parameters} representing {@code value}.
     */
    public static Parameters of(Map<String, Object> value) {
        return new Parameters(value);
    }

    /**
     * Creates an unmodifiable {@link Parameters} instance representing
     * the specified {@linkplain Object}s.
     * @param obs (needs to be an even-number of {@linkplain Object}s)
     * @return a {@link Parameters} representing {@code obs}.
     */
    public static Parameters valueOf(Object... obs) {
        if (obs.length == 1 && obs[0] instanceof Map) {
            throw new IllegalArgumentException("requires even number of arguments, got: " +
                    obs[0].getClass().getName() + " - did you mean to use 'of()'?");
        }
        if (obs.length % 2 != 0) {
            throw new IllegalArgumentException("requires even number of arguments, got: " + obs.length);
        } else {
            Map<String, Object> map = new LinkedHashMap<>();
            for (int i = 0; i < obs.length; i += 2) {
                String key = obs[i].toString();
                if (map.containsKey(key)) {
                    throw new IllegalArgumentException("key " + key + " already exists");
                }
                map.put(key, obs[i + 1]);
            }
            return of(map);
        }
    }

    /**
     * Serialize this parameter to a {@linkplain StringBuilder}
     * @param sb to serialize to
     * @return updated {@linkplain StringBuilder}
     */
    public StringBuilder serializeTo(StringBuilder sb) {
        for (Map.Entry<String, Item<?>> e : delegate.entrySet()) {
            sb.append(';').append(e.getKey());
            if (!(e.getValue().get().equals(Boolean.TRUE))) {
                sb.append('=');
                e.getValue().serializeTo(sb);
            }
        }
        return sb;
    }

    /**
     * Serialize this parameter.
     * @return serialization
     */
    public String serialize() {
        return serializeTo(new StringBuilder()).toString();
    }

    /**
     * Serialize debug information to an existing {@link StringBuilder}.
     *
     * @param sb
     *            where to serialize to
     * @param indentLevel how much to indent
     * @param classFormatter to format the classname when desires (can be a function that returns an empty string)
     * @return the {@link StringBuilder} so calls can be chained.
     */
    public StringBuilder serializeToForDebug(StringBuilder sb, int indentLevel, Function<Class, String> classFormatter) {
        if (!delegate.isEmpty()) {
            String indent = indentLevel != 0 ? String.format("%" + indentLevel + "s", "") : "";
            String classn = classFormatter.apply(this.getClass());
            sb.append(indent).append(serialize()).append(classn).append("\n");
            for (Map.Entry<String, Item<?>> e : delegate.entrySet()) {
                sb.append("  " + indent).append(e.getKey()).append(" -> ");
                e.getValue().serializeToForDebug(sb, 0, classFormatter);
            }
            return sb;
        } else {
            return sb;
        }
    }

    private static Map<String, Item<?>> checkAndTransformMap(Map<String, Object> map) {
        Map<String, Item<?>> result = new LinkedHashMap<>(
                Objects.requireNonNull(map, "Map must not be null").size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = Utils.checkKey(entry.getKey());
            Item<?> value = Utils.asBareItem(entry.getValue());
            if (!value.params().isEmpty()) {
                throw new IllegalArgumentException("Parameter value for '" + key + "' must be bare item (no parameters)");
            }
            result.put(entry.getKey(), value);
        }
        return result;
    }

    // delegate methods, autogenerated

    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item<?> compute(String key,
                           BiFunction<? super String, ? super Item<?>, ? extends Item<?>> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item<?> computeIfAbsent(String key,
                                   Function<? super String, ? extends Item<?>> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item<?> computeIfPresent(String key,
                                    BiFunction<? super String, ? super Item<?>, ? extends Item<?>> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    public Set<Entry<String, Item<?>>> entrySet() {
        return delegate.entrySet();
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Item<?>> action) {
        delegate.forEach(action);
    }

    public Item<?> get(Object key) {
        return delegate.get(key);
    }

    @Override
    public Item<?> getOrDefault(Object key, Item<?> defaultValue) {
        return delegate.getOrDefault(key, defaultValue);
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public Set<String> keySet() {
        return delegate.keySet();
    }

    @Override
    public Item<?> merge(String key, Item<?> value,
            BiFunction<? super Item<?>, ? super Item<?>, ? extends Item<?>> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public Item<?> put(String key, Item<?> value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends String, ? extends Item<?>> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item<?> putIfAbsent(String key, Item<?> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public Item<?> remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(String key, Item<?> oldValue, Item<?> newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item<?> replace(String key, Item<?> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Item<?>, ? extends Item<?>> function) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return delegate.size();
    }

    public Collection<Item<?>> values() {
        return delegate.values();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Parameters)) {
            return false;
        } else {
            Parameters that = (Parameters) o;
            return Objects.equals(delegate, that.delegate);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(delegate);
    }
}
