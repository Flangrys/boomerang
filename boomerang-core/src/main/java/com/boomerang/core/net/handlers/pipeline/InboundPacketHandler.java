package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.core.net.handlers.BoomerangChannelState;
import com.boomerang.proto.ConnectionState;
import com.boomerang.proto.packets.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class InboundPacketHandler extends ChannelInboundHandlerAdapter {
    public static final String HANDLER_NAME = "inbound_packet_handler";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Packet packet) {
            final ConnectionState connectionState = ctx.channel().attr(BoomerangChannelState.CLIENT_CONNECTION_STATE).get();

            switch (connectionState) {
                case HANDSHAKE -> {
                    addHandlerSafely(ctx, InboundHandshakeHandler.HANDLER_NAME, new InboundHandshakeHandler());
                }

                case STATUS -> {
                    removeHandlerSafely(ctx, InboundHandshakeHandler.HANDLER_NAME);
                    addHandlerSafely(ctx, InboundStatusHandler.HANDLER_NAME, new InboundStatusHandler());
                }

                case LOGIN -> {
                    removeHandlerSafely(ctx, InboundHandshakeHandler.HANDLER_NAME);
                    removeHandlerSafely(ctx, InboundStatusHandler.HANDLER_NAME);
                }

                case CONFIGURATION -> {
                }

                case PLAY -> {
                }
            }

            ctx.fireChannelRead(packet);

        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private static void addHandlerSafely(ChannelHandlerContext ctx, String name, ChannelHandler handler) {
        final var pipelineHandlerContext = ctx.pipeline().context(name);

        if (pipelineHandlerContext == null) {
            ctx.pipeline().addAfter(HANDLER_NAME, name, handler);
        }
    }

    private static void removeHandlerSafely(ChannelHandlerContext ctx, String name) {
        final var pipelineHandler = ctx.pipeline().get(name);

        if (pipelineHandler != null) {
            ctx.pipeline().remove(pipelineHandler);
        }
    }
}
