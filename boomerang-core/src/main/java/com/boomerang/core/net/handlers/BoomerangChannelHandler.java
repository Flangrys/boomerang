package com.boomerang.core.net.handlers;

import com.boomerang.core.net.handlers.pipeline.*;
import com.boomerang.proto.ConnectionState;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public final class BoomerangChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.attr(BoomerangChannelState.CLIENT_CONNECTION_STATE).set(ConnectionState.HANDSHAKE);

        channel.pipeline()
                .addFirst("outbound_packet_frame_encoder", new OutboundPacketFrameEncoder())
                .addLast("inbound_legacy_ping_packet_handler", new InboundLegacyPingPacketHandler())
                .addLast("inbound_packet_frame_handler", new InboundPacketFrameHandler())
                .addLast("inbound_packet_decoder_handler", new InboundPacketDecoderHandler())
                .addLast("inbound_packet_handler", new InboundPacketHandler());
    }
}
