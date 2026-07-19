package com.boomerang.core.net.handlers;

import com.boomerang.core.net.handlers.pipeline.*;
import com.boomerang.proto.ConnectionState;
import com.boomerang.proto.exceptions.CodecException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DecoderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@ChannelHandler.Sharable
public final class BoomerangChannelHandler extends ChannelInitializer<SocketChannel> {

    public static final Logger logger = LogManager.getLogger(BoomerangChannelHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final var clientAddress = ctx.channel().remoteAddress();

        logger.info("New connection established: {}", clientAddress);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final var clientAddress = ctx.channel().remoteAddress();

        logger.info("Connection closed: {}", clientAddress);
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.attr(BoomerangChannelState.CLIENT_CONNECTION_STATE).set(ConnectionState.HANDSHAKE);

        channel.pipeline()
                .addLast(BoomerangTrafficLogger.HANDLER_NAME, new BoomerangTrafficLogger())
                .addLast(InboundLegacyPingPacketHandler.HANDLER_NAME, new InboundLegacyPingPacketHandler())
                .addLast(InboundPacketFrameHandler.HANDLER_NAME, new InboundPacketFrameHandler())
                .addLast(InboundPacketDecoderHandler.HANDLER_NAME, new InboundPacketDecoderHandler())
                .addLast(InboundPacketHandler.HANDLER_NAME, new InboundPacketHandler())
                .addLast(OutboundPacketFrameHandler.HANDLER_NAME, new OutboundPacketFrameHandler())
                .addLast(OutboundPacketEncoderHandler.HANDLER_NAME, new OutboundPacketEncoderHandler());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        switch (cause) {
            case CodecException codecException ->
                    logger.error("An exception was caught within Boomerang pipeline");

            case DecoderException nettyDecoException ->
                    logger.error("An exception was caught within Netty pipeline");

            case IOException ioException ->
                    logger.error("An IO exception was caught");

            case null, default ->
                    logger.error("An unexpected exception was caught", cause);
        }
    }
}
