package com.boomerang.plugin.exceptions;

public class PluginOperaionException extends Exception {

    public PluginOperaionException(String message) {
        super(message);
    }

    public PluginOperaionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginOperaionException(Throwable cause) {
        super(cause);
    }
}
