package com.boomerang.core;

import com.boomerang.proto.Namespace;
import com.boomerang.proto.packets.client.PongResponsePacket;
import com.boomerang.proto.packets.client.StatusResponsePacket;
import com.boomerang.proto.packets.server.HandshakePacket;
import com.boomerang.proto.packets.server.PingRequestPacket;
import com.boomerang.proto.packets.server.StatusRequestPacket;
import com.boomerang.proto.registry.ClientboundPacketRegistry;
import com.boomerang.proto.registry.ServerboundPacketRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class BoomerangRegistryBackend implements Service {

    public static final Logger logger = LogManager.getLogger(BoomerangRegistryBackend.class);

    @Override
    public void start() {
        ServerboundPacketRegistry.HANDSHAKE.register(Namespace.fromMinecraft("intention"), 0x0, HandshakePacket.CODEC);

        ServerboundPacketRegistry.STATUS.register(Namespace.fromMinecraft("status_request"), 0x0, StatusRequestPacket.CODEC);
        ServerboundPacketRegistry.STATUS.register(Namespace.fromMinecraft("ping_request"), 0x01, PingRequestPacket.CODEC);

        ClientboundPacketRegistry.STATUS.register(Namespace.fromMinecraft("status_response"), 0x0, StatusResponsePacket.CODEC);
        ClientboundPacketRegistry.STATUS.register(Namespace.fromMinecraft("pong_response"), 0x01, PongResponsePacket.CODEC);
    }

    @Override
    public void stop() {

    }
}
