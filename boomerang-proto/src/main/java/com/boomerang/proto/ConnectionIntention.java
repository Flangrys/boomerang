package com.boomerang.proto;

import org.jetbrains.annotations.Nullable;

public enum ConnectionIntention {
    STATUS(1),
    LOGIN(2),
    TRANSFER(3);

    private final int id;

    ConnectionIntention(int id) {
        this.id = id;
    }

    public static @Nullable ConnectionIntention fromValue(int value) {
        return switch (value) {
            case 1 -> STATUS;
            case 2 -> LOGIN;
            case 3 -> TRANSFER;
            default -> null;
        };
    }

    public int intention() {
        return this.id;
    }
}
