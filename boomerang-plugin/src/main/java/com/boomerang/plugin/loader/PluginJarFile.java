package com.boomerang.plugin.loader;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public final class PluginJarFile extends JarFile {
    public PluginJarFile(File file) throws IOException {
        super(file);
    }
}
