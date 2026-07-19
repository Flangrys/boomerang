package com.boomerang.proto;

import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public record Namespace(
        @Pattern(DOMAIN_LIKE_REGEX) String domain,
        @Pattern(RESOURCE_LIKE_REGEX) String resource
) {

    @RegExp
    public static final String MINECRAFT = "minecraft";

    @RegExp
    public static final String BOOMERANG = "boomerang";

    @RegExp
    public static final String NAMESPACE_SEPARATORS = "[./]";

    @RegExp
    public static final String NAMESPACE_DIVIDER = ":";

    @RegExp
    public static final String NAMESPACE_INDENT = "[a-z0-9_-]+";

    @RegExp
    public static final String DOMAIN_LIKE_REGEX = "^#?[a-z0-9_-]+";

    @RegExp
    public static final String RESOURCE_LIKE_REGEX = NAMESPACE_INDENT + "(?:[./][a-z0-9_-]+)*$";

    @RegExp
    public static final String NAMESPACE_REGEX = DOMAIN_LIKE_REGEX + ":" + RESOURCE_LIKE_REGEX;

    private static boolean testLegalCharactersInNamespace(int c) {
        final var isComponent = c == '_' || c == '.' || c == '-' || c == '/' || c == ':';

        return c == '#' || Character.isLetterOrDigit(c) || isComponent;
    }

    public Namespace {
        if (domain == null || domain.isBlank()) {
            throw new IllegalArgumentException("Namespaces should start with namespace valid domain");
        }

        if (resource == null || resource.isBlank()) {
            throw new IllegalArgumentException("Namespaces should start with namespace valid resource");
        }

        domain = domain.toLowerCase(Locale.ENGLISH);
        resource = resource.toLowerCase(Locale.ENGLISH);
    }

    public boolean isTag() {
        return this.domain.charAt(0) == '#';
    }

    public Schema getSchema() {
        final var parent = this.isTag()
                ? this.domain.subSequence(1, 9)
                : this.domain.subSequence(0, 8);

        return switch (parent.toString()) {
            case MINECRAFT -> Schema.MINECRAFT;
            case BOOMERANG -> Schema.BOOMERANG;
            default -> Schema.OTHER;
        };
    }

    @Override
    @NotNull
    public String toString() {
        return domain + ":" + resource;
    }

    public static Namespace fromString(String namespaceMaybe) {
        final boolean validNamespaceCharacters = namespaceMaybe.chars()
                .map(c -> (char) c)
                .anyMatch(Namespace::testLegalCharactersInNamespace);

        if (validNamespaceCharacters) {
            throw new IllegalArgumentException("Illegal characters in namespace. Only allowed [namespace-z0-9/._-] : " + namespaceMaybe);
        }

        final var namespace = namespaceMaybe.split(":");

        if (namespace.length != 2) {
            throw new IllegalArgumentException("Invalid namespace format provided: " + namespaceMaybe);
        }

        return new Namespace(namespace[0], namespace[1]);
    }

    public static Namespace fromMinecraft(@Pattern(RESOURCE_LIKE_REGEX) String resource) {
        return new Namespace(MINECRAFT, resource);
    }

    public static Namespace fromBoomerang(@Pattern(RESOURCE_LIKE_REGEX) String resource) {
        return new Namespace(BOOMERANG, resource);
    }

    public static enum Schema {
        MINECRAFT, BOOMERANG, OTHER;
    }
}
