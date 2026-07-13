package com.boomerang.proto.packets;

public interface TerminalPacket extends Packet {

    default boolean isTerminal() {
        return true;
    }
}
