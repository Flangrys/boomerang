package com.boomerang.core.net.handlers;

import io.netty.handler.logging.ByteBufFormat;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public final class BoomerangTrafficLogger extends LoggingHandler {
    public static final String HANDLER_NAME = "boomerang_traffic_logger";

    public BoomerangTrafficLogger() {
        super(LogLevel.DEBUG, ByteBufFormat.SIMPLE);
    }
}
