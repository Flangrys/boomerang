package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.proto.types.Numeric;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class InboundPacketFrameHandler extends ByteToMessageDecoder {
    private static final Logger logger = LogManager.getLogger(InboundPacketFrameHandler.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final int packetSize = Numeric.VARINT.read(in);

        if (!in.isReadable(packetSize)) {
            logger.warn("Unable determine the frame size for this packet.");

        } else {
            final var packetFrame = ctx.alloc().buffer(packetSize);

            in.readBytes(packetFrame);

            out.add(packetFrame);
        }
    }
}
