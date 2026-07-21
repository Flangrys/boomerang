package com.boomerang.plugin.exceptions;

public class PluginManifestException extends PluginOperaionException {
    public PluginManifestException(String message) {
        super(message);
    }

    public PluginManifestException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginManifestException(Throwable cause) {
        super(cause);
    }
}
