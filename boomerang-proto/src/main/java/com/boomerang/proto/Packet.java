package com.boomerang.proto;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Packet {

    int id();

    ConnectionBound bound();

    ConnectionState state();

    Type<? extends Packet> codec();
}
