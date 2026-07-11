package com.boomerang.proto.packets;

import com.boomerang.proto.Packet;

public interface TerminalPacket extends Packet {

    default boolean isTerminal() {
        return true;
    }
}
