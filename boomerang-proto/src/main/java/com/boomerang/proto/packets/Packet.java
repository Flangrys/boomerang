package com.boomerang.proto.packets;

import com.boomerang.proto.ConnectionBound;
import com.boomerang.proto.ConnectionState;
import com.boomerang.proto.Identified;

public interface Packet extends Identified {

    ConnectionBound bound();

    ConnectionState state();

    interface Handshake extends Packet {
        @Override
        default ConnectionState state() {
            return ConnectionState.HANDSHAKE;
        }
    }

    interface Login extends Packet {
        @Override
        default ConnectionState state() {
            return ConnectionState.LOGIN;
        }
    }

    interface Status extends Packet {
        @Override
        default ConnectionState state() {
            return ConnectionState.STATUS;
        }
    }

    interface Configuration extends Packet {
        @Override
        default ConnectionState state() {
            return ConnectionState.CONFIGURATION;
        }
    }

    interface Play extends Packet {
        @Override
        default ConnectionState state() {
            return ConnectionState.PLAY;
        }
    }
}
