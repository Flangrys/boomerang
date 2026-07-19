package com.boomerang.proto.registry;

import com.boomerang.proto.codecs.Codec;
import com.boomerang.proto.packets.Packet;

public abstract class PacketRegistry<P extends Packet> extends AbstractRegistry<Codec<? extends P>> {
}
