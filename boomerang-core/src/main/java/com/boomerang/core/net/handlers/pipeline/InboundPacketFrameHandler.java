package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.proto.types.Numeric;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CodecException;

import java.util.List;

public class InboundPacketFrameHandler extends ByteToMessageDecoder {
    public static final String HANDLER_NAME = "inbound_packet_frame_handler";
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!in.isReadable()) {
            return;
        }

        try {
            in.markReaderIndex();

            final int readableSize = in.readableBytes();
            final int packetSize = Numeric.VARINT.read(in);

            if (packetSize != 0 && in.isReadable(packetSize)) {
                final ByteBuf packetFrame = in.readRetainedSlice(packetSize);

                out.add(packetFrame);

            } else {
                in.resetReaderIndex();
            }

        } catch (CodecException e) {
            in.resetReaderIndex();
        }
    }
}
