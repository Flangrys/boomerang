package com.boomerang.plugin.exceptions;

public class PluginClassLoaderException extends PluginLoaderException {
    public PluginClassLoaderException(String message) {
        super(message);
    }

    public PluginClassLoaderException(String message, ClassLoader loader) {
        this(message + ": " + loader.getClass().getName());
    }
}
