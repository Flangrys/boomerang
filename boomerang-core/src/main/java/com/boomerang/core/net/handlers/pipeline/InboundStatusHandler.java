package com.boomerang.core.net.handlers.pipeline;

import com.boomerang.proto.packets.Packet;
import com.boomerang.proto.packets.client.PongResponsePacket;
import com.boomerang.proto.packets.client.StatusResponsePacket;
import com.boomerang.proto.packets.server.PingRequestPacket;
import com.boomerang.proto.packets.server.StatusRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InboundStatusHandler extends SimpleChannelInboundHandler<Packet.Status> {
    public static final Logger logger = LogManager.getLogger(InboundStatusHandler.class);
    public static final String HANDLER_NAME = "status_handler_adapter";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet.Status msg) throws Exception {
        switch (msg) {
            case StatusRequestPacket status -> {
                    ctx.channel().writeAndFlush(StatusResponsePacket.fromDefaultServerStatus());
            }

            case PingRequestPacket ping ->
                    ctx.channel().writeAndFlush(PongResponsePacket.pong(ping));

            default -> {
                logger.info("No handler associated for the message: {}", msg);
            }
        }
    }
}
