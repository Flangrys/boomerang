package com.boomerang.core.net.handlers;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;

@ChannelHandler.Sharable
public class BoomerangTrafficMonitor extends ChannelDuplexHandler {
    public static final String HANDLER_NAME = "boomerang_traffic_monitor";
}
