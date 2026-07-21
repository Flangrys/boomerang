package com.boomerang.plugin.exceptions;

public class PluginLoaderException extends PluginOperaionException {
    public PluginLoaderException(String message) {
        super(message);
    }

    public PluginLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoaderException(Throwable cause) {
        super(cause);
    }
}
