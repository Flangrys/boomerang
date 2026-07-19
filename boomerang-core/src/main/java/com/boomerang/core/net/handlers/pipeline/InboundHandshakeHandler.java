package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.core.net.handlers.BoomerangChannelState;
import com.boomerang.proto.ConnectionState;
import com.boomerang.proto.packets.server.HandshakePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public final class InboundHandshakeHandler extends SimpleChannelInboundHandler<HandshakePacket> {
    public static final String HANDLER_NAME = "handshake_handler_adapter";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HandshakePacket packet) throws Exception {
        final var clientIntention = packet.intentions();

        final var state = ctx.channel().attr(BoomerangChannelState.CLIENT_CONNECTION_STATE);

        switch (clientIntention) {
            case TRANSFER -> state.set(ConnectionState.CONFIGURATION);
            case STATUS -> state.set(ConnectionState.STATUS);
            case LOGIN -> state.set(ConnectionState.LOGIN);
        }
    }
}
