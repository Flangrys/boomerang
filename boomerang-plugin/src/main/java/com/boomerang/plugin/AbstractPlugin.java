package com.boomerang.plugin;

import com.boomerang.plugin.exceptions.PluginOperaionException;
import com.boomerang.plugin.loader.PluginClassLoader;

import java.io.File;
import java.util.Objects;

public abstract class AbstractPlugin implements Pluggable {

    private final PluginManifest pluginManifest;

    public AbstractPlugin() throws PluginOperaionException {
        final ClassLoader pluginClassLoader = PluginClassLoader.retrievePluginClassLoader(this);

        this.pluginManifest = PluginManifest.readManifest(new File("/manifest.properties"));
    }

    @Override
    public PluginManifest getPluginManifest() {
        return this.pluginManifest;
    }

    @Override
    public String getPluginVersion() {
        return this.getPluginManifest().version();
    }

    @Override
    public String getPluginName() {
        return this.getPluginManifest().name();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.getPluginManifest().name(),
                this.getPluginManifest().version()
        );
    }
}
