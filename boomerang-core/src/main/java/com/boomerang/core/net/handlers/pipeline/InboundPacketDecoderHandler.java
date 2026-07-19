package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.core.net.handlers.BoomerangChannelState;
import com.boomerang.proto.exceptions.CodecException;
import com.boomerang.proto.registry.ServerboundPacketRegistry;
import com.boomerang.proto.types.Numeric;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class InboundPacketDecoderHandler extends MessageToMessageDecoder<ByteBuf> {
    public static final String HANDLER_NAME = "inbound_packet_decoder_handler";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        final var state = ctx.channel().attr(BoomerangChannelState.CLIENT_CONNECTION_STATE).get();

        final var packetId = Numeric.VARINT.read(buffer);
        final var packetCodec = ServerboundPacketRegistry.ofState(state).ofId(packetId);

        if (packetCodec == null) {
            throw new CodecException("No codec associated with packet id " + packetId, state);
        }

        final var packetDecoded = packetCodec.read(buffer);

        out.add(packetDecoded);
    }
}
