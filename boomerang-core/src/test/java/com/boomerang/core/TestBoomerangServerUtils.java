package com.boomerang.core;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static com.boomerang.core.utils.Favicon.loadFavicon;
import static org.junit.jupiter.api.Assertions.*;

class TestBoomerangServerUtils {

    @Test
    public void test_BoomerangServerLoadFavicon_When_FaviconFileExist_Expecting_NoExceptionThrown() {
        assertDoesNotThrow(() -> loadFavicon("/server-icon.png"));
    }

    @Test
    public void test_BoomerangServerLoadFavicon_When_FaviconFileExist_Expecting_DoesPrintEncodedBase64() {
        AtomicReference<String> favicon = new AtomicReference<>();

        assertDoesNotThrow(() -> favicon.set(loadFavicon("/server-icon.png")));

        assertNotEquals("", favicon.get());

        System.out.println(favicon.get());
    }
}