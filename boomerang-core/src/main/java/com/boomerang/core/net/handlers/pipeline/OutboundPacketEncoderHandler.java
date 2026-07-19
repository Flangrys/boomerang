package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.core.net.handlers.BoomerangChannelState;
import com.boomerang.proto.codecs.Codec;
import com.boomerang.proto.exceptions.CodecException;
import com.boomerang.proto.packets.ClientboundPacket;
import com.boomerang.proto.registry.ClientboundPacketRegistry;
import com.boomerang.proto.types.Numeric;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class OutboundPacketEncoderHandler extends MessageToByteEncoder<ClientboundPacket> {
    public static final String HANDLER_NAME = "outbound_packet_encoder_handler";

    @Override
    @SuppressWarnings("unchecked")
    protected void encode(ChannelHandlerContext ctx, ClientboundPacket msg, ByteBuf out) throws Exception {
        final var state = ctx.channel().attr(BoomerangChannelState.CLIENT_CONNECTION_STATE).get();
        final var registry = ClientboundPacketRegistry.ofState(state);

        final Codec<ClientboundPacket> codec = (Codec<ClientboundPacket>) registry.ofId(msg.id());

        if (codec == null) {
            throw new CodecException("No codec associated with packet id " + msg.id());
        }

        Numeric.VARINT.write(out, msg.id());

        codec.write(out, msg);
    }
}
