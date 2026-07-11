package com.boomerang.proto.packets;

import com.boomerang.proto.ConnectionIntention;
import com.boomerang.proto.Packet;

public interface SwitchingProtocolsPacket extends Packet {

    ConnectionIntention intentions();
}
