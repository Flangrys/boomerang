package com.boomerang.proto.packets.server;

import com.boomerang.proto.codecs.Codec;
import com.boomerang.proto.packets.ServerboundPacket;

public record StatusRequestPacket() implements ServerboundPacket.Status, ServerboundPacket {
    public static final Codec<StatusRequestPacket> CODEC = Codec.template(new StatusRequestPacket(), 0x0);

    @Override
    public int id() {
        return 0x0;
    }
}
