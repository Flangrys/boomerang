package com.boomerang.proto.packets.client;

import com.boomerang.proto.codecs.Codec;
import com.boomerang.proto.packets.ClientboundPacket;
import com.boomerang.proto.packets.server.PingRequestPacket;
import com.boomerang.proto.types.Numeric;

public record PongResponsePacket(Long timestamp) implements ClientboundPacket.Status, ClientboundPacket {
    public static final Codec<PongResponsePacket> CODEC = Codec.template(
            Numeric.LONG, PongResponsePacket::timestamp, PongResponsePacket::new, 0x1
    );

    public static PongResponsePacket pong(PingRequestPacket ping) {
        return new PongResponsePacket(ping.timestamp());
    }

    @Override
    public int id() {
        return 0x1;
    }
}
