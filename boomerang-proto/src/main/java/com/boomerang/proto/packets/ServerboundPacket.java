package com.boomerang.proto.packets;

import com.boomerang.proto.ConnectionBound;
import com.boomerang.proto.ConnectionState;
import com.boomerang.proto.Packet;

public interface ServerboundPacket extends Packet {

    @Override
    default ConnectionBound bound() {
        return ConnectionBound.SERVERBOUND;
    }

    interface Handshake extends ServerboundPacket {
        @Override
        default ConnectionState state() {
            return ConnectionState.HANDSHAKE;
        }
    }

    interface Login extends ServerboundPacket {
        @Override
        default ConnectionState state() {
            return ConnectionState.LOGIN;
        }
    }

    interface Status extends ServerboundPacket {
        @Override
        default ConnectionState state() {
            return ConnectionState.STATUS;
        }
    }

    interface Configuration extends ServerboundPacket {
        @Override
        default ConnectionState state() {
            return ConnectionState.CONFIGURATION;
        }
    }

    interface Play extends ServerboundPacket {
        @Override
        default ConnectionState state() {
            return ConnectionState.PLAY;
        }
    }
}
