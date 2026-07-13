package com.boomerang.proto.registry;

import com.boomerang.proto.Namespace;
import com.boomerang.proto.codecs.Codec;
import com.boomerang.proto.packets.Packet;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PacketRegistry<T extends Packet> implements Registry<Namespace, Codec<T>> {
    private final Map<Namespace, Codec<T>> codecs = new ConcurrentHashMap<>();

    public static final Registry<Namespace, Codec<Packet.Configuration>> CONFIGURATION = new PacketRegistry<>();
    public static final Registry<Namespace, Codec<Packet.Handshake>> HANDSHAKE = new PacketRegistry<>();
    public static final Registry<Namespace, Codec<Packet.Status>> STATUS = new PacketRegistry<>();
    public static final Registry<Namespace, Codec<Packet.Login>> LOGIN = new PacketRegistry<>();
    public static final Registry<Namespace, Codec<Packet.Play>> PLAY = new PacketRegistry<>();

    @Override
    public Codec<T> register(Namespace key, Codec<T> value) {
        return this.codecs.put(key, value);
    }

    @Override
    public Codec<T> unregister(Namespace key) {
        return this.codecs.remove(key);
    }

    @Override
    public List<Codec<T>> records() {
        return this.codecs.values().stream().toList();
    }
}
