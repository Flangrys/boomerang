package com.boomerang.proto.packets;

import com.boomerang.proto.ConnectionIntention;

public interface IntentionPacket extends Packet {

    ConnectionIntention intentions();
}
