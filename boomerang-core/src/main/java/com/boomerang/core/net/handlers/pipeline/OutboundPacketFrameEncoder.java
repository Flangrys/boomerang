package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.proto.packets.ClientboundPacket;
import com.boomerang.proto.types.Numeric;
import com.boomerang.proto.types.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class OutboundPacketFrameEncoder extends MessageToByteEncoder<ClientboundPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ClientboundPacket packet, ByteBuf out) throws Exception {

    }
}
