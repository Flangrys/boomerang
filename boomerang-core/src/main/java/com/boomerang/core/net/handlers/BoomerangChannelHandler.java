package com.boomerang.core.net.handlers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public final class BoomerangChannelHandler extends ChannelInitializer<SocketChannel> {

    /**
     * TODO: Implementar cada uno de los handlers utilizados en el inicializador del canal.
     */
    @Override
    protected void initChannel(SocketChannel sockChannel) throws Exception {

        sockChannel.pipeline()
                .addFirst("outbound_packet_frame_encoder", null)
                .addLast("inbound_packet_frame_handler", null)
                .addLast("inbound_packet_decoder_handler", null)
                .addLast("inbound_packet_handler", null);
    }
}
