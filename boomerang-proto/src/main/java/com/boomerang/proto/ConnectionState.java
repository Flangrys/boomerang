package com.boomerang.proto;

public enum ConnectionState {

    HANDSHAKE(0),
    STATUS(1),
    LOGIN(2),
    CONFIGURATION(3),
    PLAY(4);

    private final int serial;

    ConnectionState(int serial) {
        this.serial = serial;
    }

    public int serial() {
        return serial;
    }
}
