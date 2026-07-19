package com.boomerang.proto.registry;

import com.boomerang.proto.ConnectionState;
import com.boomerang.proto.packets.ServerboundPacket;

public final class ServerboundPacketRegistry extends PacketRegistry<ServerboundPacket> {
    public static final ServerboundPacketRegistry CONFIGURATION = new ServerboundPacketRegistry();
    public static final ServerboundPacketRegistry HANDSHAKE = new ServerboundPacketRegistry();
    public static final ServerboundPacketRegistry STATUS = new ServerboundPacketRegistry();
    public static final ServerboundPacketRegistry LOGIN = new ServerboundPacketRegistry();
    public static final ServerboundPacketRegistry PLAY = new ServerboundPacketRegistry();

    public static ServerboundPacketRegistry ofState(ConnectionState state) {
        return switch (state) {
            case CONFIGURATION -> ServerboundPacketRegistry.CONFIGURATION;
            case HANDSHAKE -> ServerboundPacketRegistry.HANDSHAKE;
            case STATUS -> ServerboundPacketRegistry.STATUS;
            case LOGIN -> ServerboundPacketRegistry.LOGIN;
            case PLAY -> ServerboundPacketRegistry.PLAY;
        };
    }
}
