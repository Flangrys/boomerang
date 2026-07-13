package com.boomerang.core.net;

import com.boomerang.core.Service;
import com.boomerang.core.net.handlers.BoomerangChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class BoomerangNetworkBackend implements Service {
    private static final Logger logger = LogManager.getLogger(BoomerangNetworkBackend.class);

    private final SocketAddress serverAddress;
    private final ServerBootstrap serverBootstrap;
    private final NioEventLoopGroup serverBossGroup;
    private final NioEventLoopGroup serverWorkerGroup;


    public BoomerangNetworkBackend(String serverHost, int serverPort) {
        if (serverHost == null || serverHost.isBlank()) {
            throw new IllegalArgumentException("serverHost must be provided");
        }

        if (serverPort < 0 || serverPort > 65535) {
            throw new IllegalArgumentException("serverPort must be between 0 and 65535");
        }

        this.serverAddress = new InetSocketAddress(serverHost, serverPort);
        this.serverBossGroup = new NioEventLoopGroup(1);
        this.serverWorkerGroup = new NioEventLoopGroup();
        this.serverBootstrap = new ServerBootstrap();
    }

    public BoomerangNetworkBackend(int serverPort) {
        this("localhost", serverPort);
    }

    public BoomerangNetworkBackend() {
        this("localhost", 25565);
    }

    @Override
    public void start() {
        logger.info("Starting Boomerang Network Backend...");

        this.serverBootstrap
                .group(this.serverBossGroup, this.serverWorkerGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(this.serverAddress)
                .childHandler(new BoomerangChannelHandler())
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            // Almacena el futuro de este canal que sera notificado cuando haya una nueva conexion.
            final ChannelFuture channelFuture = this.serverBootstrap.bind().sync();

            // Almacena el futuro de este canal que sera notificado cuando se cierre este canal.
            final ChannelFuture closeChannelFuture = channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException exc) {
            throw new RuntimeException("Something occurred at the network execution thread", exc);
        }


    }

    @Override
    public void stop() {
        logger.info("Stopping Boomerang network backend...");

        // Almacena el futuro del grupo de jefes que sera notificado cuando se apagen.
        final Future<?> serverWorkerGroupFuture = this.serverWorkerGroup.shutdownGracefully();

        // Almacena el futuro del grupo de trabajadores que sera notificado cuando se apagen.
        final Future<?> serverBoosGroupFuture = this.serverBossGroup.shutdownGracefully();

        logger.info("Network backend stopped.");
    }
}
