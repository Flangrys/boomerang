package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.core.net.handlers.BoomerangChannelState;
import com.boomerang.proto.ConnectionState;
import com.boomerang.proto.types.Numeric;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CodecException;

import java.util.List;

public class InboundLegacyPingPacketHandler extends ByteToMessageDecoder {
    public static final String HANDLER_NAME = "inbound_legacy_ping_packet_handler";

    public static final int LEGACY_PING_PACKET_ID = 0xFE;
    public static final int LEGACY_PING_PACKET_PAYLOAD = 0x01;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final ConnectionState state = ctx.channel().attr(BoomerangChannelState.CLIENT_CONNECTION_STATE).get();

        if (state == ConnectionState.HANDSHAKE) {
            try {
                in.markReaderIndex();

                int readPacketID = Numeric.VARINT.read(in);
                int readPacketPayload = Numeric.UBYTE.read(in);

                if (readPacketID == LEGACY_PING_PACKET_ID && readPacketPayload == LEGACY_PING_PACKET_PAYLOAD) {
                    return;
                }

            } catch (CodecException exc) {
                in.resetReaderIndex();
            }

            in.resetReaderIndex();
            ctx.fireChannelRead(in.retain());

        } else {
            ctx.fireChannelRead(in.retain());
        }
    }
}
