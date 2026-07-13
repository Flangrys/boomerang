package com.boomerang.core;

public class BoomerangThread extends Thread {

    public static Builder.OfPlatform ofPlatform(String prefix) {
        return Thread.ofPlatform().daemon(false).name(prefix);
    }

    public static Builder.OfVirtual ofVirtual(String prefix) {
        return Thread.ofVirtual().name(prefix);
    }
}
