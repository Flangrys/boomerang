package com.boomerang.proto.codecs;

import com.boomerang.proto.packets.Packet;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Codec<P extends Packet> {
    void write(@NotNull ByteBuf buffer, P packet) throws IOException;

    P read(@NotNull ByteBuf buffer) throws IOException;
}
