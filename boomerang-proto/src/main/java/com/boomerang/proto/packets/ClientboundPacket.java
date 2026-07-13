package com.boomerang.proto.packets;

import com.boomerang.proto.ConnectionBound;

public interface ClientboundPacket extends Packet {
    @Override
    default ConnectionBound bound() {
        return ConnectionBound.CLIENTBOUND;
    }
}
