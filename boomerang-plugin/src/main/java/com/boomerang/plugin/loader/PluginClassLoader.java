package com.boomerang.plugin.loader;

import com.boomerang.plugin.AbstractPlugin;
import com.boomerang.plugin.PluginManifest;
import com.boomerang.plugin.exceptions.PluginClassLoaderException;
import com.boomerang.plugin.exceptions.PluginLoaderException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public final class PluginClassLoader extends URLClassLoader {
    private final PluginManifest pluginManifest;
    private final PluginJarFile pluginJarFile;

    private final ClassLoader parentLoader;
    private final ClassLoader libraryLoader;

    public final AbstractPlugin plugin;

    public PluginClassLoader(
            @NotNull PluginManifest pluginManifest,
            @NotNull ClassLoader parentLoader,
            @NotNull ClassLoader libraryLoader,
            @NotNull File pluginFile
    ) throws PluginLoaderException, MalformedURLException {
        super(new URL[]{pluginFile.toURI().toURL()}, parentLoader);

        this.pluginManifest = pluginManifest;
        this.parentLoader = parentLoader;
        this.libraryLoader = libraryLoader;

        this.pluginJarFile = null;

        try {
            this.plugin = Class
                    .forName(this.pluginManifest.classpath(), true, this)
                    .asSubclass(AbstractPlugin.class)
                    .getDeclaredConstructor()
                    .newInstance();

        } catch (ExceptionInInitializerError | InvocationTargetException err) {
            throw new PluginLoaderException("Cannot initialize plugin main class because: ", err);

        } catch (ClassNotFoundException exc) {
            throw new PluginLoaderException("Cannot find declared plugin main class", exc);

        } catch (NoSuchMethodException exc) {
            throw new PluginLoaderException("Declared plugin main class must have a public no-args constructor", exc);

        } catch (ClassCastException exc) {
            throw new PluginLoaderException("Declared plugin main class must extend from AbstractPlugin", exc);

        } catch (InstantiationException exc) {
            throw new PluginLoaderException("Declared plugin main class isn't instantiable", exc);

        } catch (IllegalAccessException exc) {
            throw new PluginLoaderException("Declared plugin main class constructor must be public", exc);
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();

        } finally {
            this.pluginJarFile.close();
        }
    }


    /**
     * Intenta recuperar la referencia al class loader de {@code plugin} si y solo si el
     * {@link ClassLoader} asociado es una instancia de {@link PluginClassLoader}. De lo contrario lanzará
     * {@link PluginLoaderException}.
     *
     * @param plugin Una referencia al plugin del que se intentara recuperar el {@link ClassLoader}.
     * @return La referencia al {@link PluginClassLoader} del plugin dado.
     * @throws PluginLoaderException Si el {@link ClassLoader} no es una instancia de {@link PluginClassLoader}.
     */
    public static ClassLoader retrievePluginClassLoader(AbstractPlugin plugin) throws PluginLoaderException {
        final var classLoader = plugin.getClass();

        if (classLoader.getClassLoader() instanceof PluginClassLoader pluginClassLoader) {
            return pluginClassLoader;
        }

        throw new PluginClassLoaderException("Invalid plugin class loader: " + classLoader);
    }
}
