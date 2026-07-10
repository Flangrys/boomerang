package com.boomerang.core;

public record Namespace(String path, String resource) {
    public Namespace {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Namespaces should start with a valid path");
        }

        if (resource == null || resource.isBlank()) {
            throw new IllegalArgumentException("Namespaces should start with a valid resource");
        }
    }
}
