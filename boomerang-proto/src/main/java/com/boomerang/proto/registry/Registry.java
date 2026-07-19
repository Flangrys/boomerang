package com.boomerang.proto.registry;

import com.boomerang.proto.Identified;
import com.boomerang.proto.Namespace;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Objects;

public interface Registry<V extends Identified> {

    V register(Namespace namespace, Integer id, V value);

    V unregister(Namespace namespace, Integer id);

    V ofKey(Namespace namespace);

    V ofId(Integer id);

    Set<V> values();

    Set<Namespace> namespaces();

    Set<Integer> ids();

    boolean containsKey(Namespace key);

    boolean containsId(Integer key);

    record Entry<A, B>(A namespace, B id) {
        @Override
        public int hashCode() {
            return Objects.hash(namespace, id);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof Entry entry) {
                return this.namespace.equals(entry.namespace) && this.id.equals(entry.id);
            }

            return false;
        }

        @Override
        public @NotNull String toString() {
            return "Entry[" + namespace + ", " + id + "]";
        }
    }
}
