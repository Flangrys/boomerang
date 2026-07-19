package com.boomerang.proto.registry;

import com.boomerang.proto.Identified;
import com.boomerang.proto.Namespace;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractRegistry<V extends Identified> implements Registry<V> {

    private final Map<Entry<Namespace, Integer>, V> codecs = new ConcurrentHashMap<>();

    public V register(Namespace namespace, Integer id, V value) {
        final var entry = new Entry<>(namespace, id);

        this.codecs.put(entry, value);

        return value;
    }

    @Override
    public V unregister(Namespace namespace, Integer id) {
        return this.codecs.remove(new Entry<>(namespace, id));
    }

    @Override
    public V ofKey(Namespace namespace) {
        return this.codecs
                .entrySet()
                .stream()
                .filter((entry) -> entry.getKey().namespace().equals(namespace))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    public V ofId(Integer id) {
        return this.codecs
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().id().equals(id))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<V> values() {
        return new HashSet<>(this.codecs.values());
    }

    @Override
    public Set<Namespace> namespaces() {
        return this.codecs
                .keySet()
                .stream()
                .map(Entry::namespace)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Integer> ids() {
        return this.codecs
                .keySet()
                .stream()
                .map(Entry::id)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean containsKey(Namespace key) {
        return this.codecs
                .keySet()
                .stream()
                .anyMatch((entry) -> entry.namespace().equals(key));
    }

    @Override
    public boolean containsId(Integer id) {
        return this.codecs
                .keySet()
                .stream()
                .anyMatch((entry) -> entry.id().equals(id));
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("[");

        for (final var entry : this.codecs.entrySet()) {
            final var key = entry.getKey();

            builder.append(key.toString());
            builder.append(", ");
        }

        builder.append("]");

        return builder.toString();
    }
}
