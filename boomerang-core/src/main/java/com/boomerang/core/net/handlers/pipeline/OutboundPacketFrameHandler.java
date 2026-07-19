package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.proto.types.Numeric;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class OutboundPacketFrameHandler extends MessageToByteEncoder<ByteBuf> {
    public static final String HANDLER_NAME = "outbound_packet_frame_handler";

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        final int packetSize = in.readableBytes();

        Numeric.VARINT.write(out, packetSize);

        out.writeBytes(in);
    }
}
