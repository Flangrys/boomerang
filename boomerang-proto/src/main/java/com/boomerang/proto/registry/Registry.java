package com.boomerang.proto.registry;

import java.util.List;

public interface Registry<K, V> {

    V register(K key, V value);

    V unregister(K key);

    List<V> records();
}
