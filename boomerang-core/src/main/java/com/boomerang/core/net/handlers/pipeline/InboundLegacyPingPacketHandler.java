package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.core.net.handlers.BoomerangChannelState;
import com.boomerang.proto.ConnectionState;
import com.boomerang.proto.types.Numeric;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class InboundLegacyPingPacketHandler extends ByteToMessageDecoder {
    private static final Logger logger = LogManager.getLogger(InboundLegacyPingPacketHandler.class);

    public static final int LEGACY_PING_PACKET_ID = 0xFE;
    public static final int LEGACY_PING_PACKET_SIZE = 55 + 56;
    public static final int LEGACY_PING_PACKET_PAYLOAD = 0x01;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final var state = ctx.channel().attr(BoomerangChannelState.CLIENT_CONNECTION_STATE).get();

        if (ConnectionState.HANDSHAKE != state) {
            return;
        }

        in.markReaderIndex();

        if (LEGACY_PING_PACKET_ID == Numeric.UBYTE.read(in)) {
            logger.info("Client {} issued a legacy ping packet request", ctx.channel().remoteAddress());

        } else {
            in.resetReaderIndex();
        }
    }
}
