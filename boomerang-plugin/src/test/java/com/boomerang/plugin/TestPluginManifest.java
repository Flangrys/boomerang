package com.boomerang.plugin;

import com.boomerang.plugin.exceptions.PluginManifestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TestPluginManifest {
    public static final File MANIFEST_FILE = new File("/manifest.properties");

    public static final AtomicReference<PluginManifest> MANIFEST = new AtomicReference<>(null);

    @Test
    void test_pluginManifestReadManifestMethod_GIVEN_AValidManifestURL_EXPECT_retrieveAFulfilledManifestInstance() {
        assertDoesNotThrow(() -> MANIFEST.set(PluginManifest.readManifest(MANIFEST_FILE)));
    }
}