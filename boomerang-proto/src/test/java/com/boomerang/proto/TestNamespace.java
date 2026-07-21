package com.boomerang.proto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

class TestNamespace {

    static Stream<Arguments> legalTestNamespaceProvider() {
        return Stream.of(
                Arguments.of("boomerang:module"),
                Arguments.of("boomerang:x-y-z.rc"),
                Arguments.of("boomerang:1-0-0.rc"),
                Arguments.of("boomerang:x.y.z-rc"),
                Arguments.of("boomerang:1.0.0-rc"),
                Arguments.of("com.boomerang:module"),
                Arguments.of("com.boomerang.module:path"),
                Arguments.of("com.boomerang.module:path-x"),
                Arguments.of("com.boomerang.module:path-x.path-y"),
                Arguments.of("com.boomerang.module:path-x-path-y"),
                Arguments.of("com.boomerang.module:path-0-path-1"),
                Arguments.of("com.boomerang.module:path.0-path.1"),
                Arguments.of("com.boomerang.module:path.0.path.1")
        );
    }

    static Stream<Arguments> illegalTestNamespaceProvider() {
        return Stream.of(
                Arguments.of(":"),
                Arguments.of("."),
                Arguments.of("/"),
                Arguments.of("#"),
                Arguments.of("@"),
                Arguments.of("X"),
                Arguments.of("0"),
                Arguments.of("#boomerang"),
                Arguments.of("#boomerang:"),
                Arguments.of("#boomerang:/"),
                Arguments.of("com.boomerang:"),
                Arguments.of("com.boomerang:."),
                Arguments.of("com.boomerang:-"),
                Arguments.of("com-boomerang:"),
                Arguments.of("com-boomerang:-."),
                Arguments.of("com/boomerang:"),
                Arguments.of("com/boomerang:."),
                Arguments.of("com/boomerang:-"),
                Arguments.of("-boomerang:module"),
                Arguments.of("-boomerang:module.0-"),
                Arguments.of("-boomerang:module.0."),
                Arguments.of("-boomerang:module/"),
                Arguments.of("/boomerang:module"),
                Arguments.of("/boomerang:module.0-"),
                Arguments.of("/boomerang:module.0."),
                Arguments.of("/boomerang:module/"),
                Arguments.of("boomerang:#"),
                Arguments.of("boomerang:#module"),
                Arguments.of("boomerang:0.module-"),
                Arguments.of("boomerang:/module")
        );
    }

    @ParameterizedTest
    @MethodSource("illegalTestNamespaceProvider")
    void testNamespaceMethod_fromStringStrict_USING_illegalNamespaces_EXPECTING_returnFalse_WHEN_anIllegalNamespaceTestFalse(String namespace) {
        assertFalse(() -> Namespace.testFullNamespaceRegex(namespace));
    }

}