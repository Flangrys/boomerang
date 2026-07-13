package com.boomerang.core.net.handlers;

import com.boomerang.proto.ConnectionState;
import io.netty.util.AttributeKey;

public final class BoomerangChannelState {

    public static final AttributeKey<ConnectionState> CLIENT_CONNECTION_STATE = AttributeKey.valueOf("CLIENT_CONNECTION_STATE");
}
