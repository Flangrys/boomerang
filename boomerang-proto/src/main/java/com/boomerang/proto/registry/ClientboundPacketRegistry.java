package com.boomerang.proto.registry;

import com.boomerang.proto.ConnectionState;
import com.boomerang.proto.packets.ClientboundPacket;

public final class ClientboundPacketRegistry extends PacketRegistry<ClientboundPacket> {
    public static final ClientboundPacketRegistry CONFIGURATION = new ClientboundPacketRegistry();
    public static final ClientboundPacketRegistry HANDSHAKE = new ClientboundPacketRegistry();
    public static final ClientboundPacketRegistry STATUS = new ClientboundPacketRegistry();
    public static final ClientboundPacketRegistry LOGIN = new ClientboundPacketRegistry();
    public static final ClientboundPacketRegistry PLAY = new ClientboundPacketRegistry();

    public static ClientboundPacketRegistry ofState(ConnectionState state) {
        return switch (state) {
            case CONFIGURATION -> ClientboundPacketRegistry.CONFIGURATION;
            case HANDSHAKE -> ClientboundPacketRegistry.HANDSHAKE;
            case STATUS -> ClientboundPacketRegistry.STATUS;
            case LOGIN -> ClientboundPacketRegistry.LOGIN;
            case PLAY -> ClientboundPacketRegistry.PLAY;
        };
    }
}
