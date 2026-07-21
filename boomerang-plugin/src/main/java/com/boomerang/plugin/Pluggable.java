package com.boomerang.plugin;

import com.boomerang.plugin.concurrent.PluginFuture;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Un plugin es una pieza de software modular que permite añadir o extender comportamientos de un servidor de
 * Minecraft Vanilla. En BoomerangMC los plugins se ven representados atravéz de {@link Pluggable}. Este establece un
 * contrato para el ciclo de vida del plugin, sus artefactos, eventos y manejadores de eventos.
 *
 * <h2>Artifacts</h2>
 * Los artefactos son recursos autogestionados por el plugin necesarios en el correcto ciclo de vidad del servidor.
 * Estos pueden ser archivos de configuracion, bases de datos en archivos, packs de texturas, datapacks, etc.
 * Generalmente cada plugin tiene su directorio de trabajo donde se encuentran estos artefactos y se pueden recuperar
 * atravéz de {@link #getPluginResource()}.
 *
 * <h2>Manifest</h2>
 * El manifiesto es un artefacto indispensable para el correcto funcionamiento de un plugin ya que define como el
 * software del servidor debera localizar y manipular el archivo.
 *
 * <p>Este mismo debe estar presente en el directorio {@code resources/manifest.properties} del proyecto.
 */
public interface Pluggable {

    /**
     * Recupera el directorio donde se alojan los recursos de este plugin.
     */
    File getPluginResource();

    /**
     * Recupera una instancia del fichero {@code manifest.properties} de ese plugin.
     */
    PluginManifest getPluginManifest();

    PluginFuture<Void> pluginLoaded(PluginServerContext ctx);

    PluginFuture<Void> pluginUnloaded(PluginServerContext ctx);

    PluginFuture<Void> pluginRegistered(PluginServerContext ctx);

    PluginFuture<Void> pluginUnregistered(PluginServerContext ctx);

    PluginFuture<Void> exceptionHandler(PluginServerContext ctx, Throwable exception);

    String getPluginName();

    String getPluginVersion();

    Logger getLogger();
}
