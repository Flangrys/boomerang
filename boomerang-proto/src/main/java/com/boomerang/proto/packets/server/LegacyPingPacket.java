package com.boomerang.proto.packets.server;

import com.boomerang.proto.packets.ServerboundPacket;
import com.boomerang.proto.packets.TerminalPacket;

public record LegacyPingPacket() implements ServerboundPacket, ServerboundPacket.Handshake, TerminalPacket {

    @Override
    public int id() {
        return 0xFE;
    }
}
