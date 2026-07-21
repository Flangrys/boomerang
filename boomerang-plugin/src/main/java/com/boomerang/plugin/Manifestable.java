package com.boomerang.plugin;


import com.boomerang.plugin.loader.PluginClassLoader;

/**
 * Representa un contenedor de informacion que imita al archivo {@code manifest.properties}. El proposito del
 * manifiesto es declarar los comportamientos del plugin como la clase principal, nombre, version y descripción del
 * plugin; y otra información relevante.
 *
 * @see PluginManifest
 * @see PluginClassLoader
 */
public interface Manifestable {

    /**
     * Guarda el nombre canonico con el que se identifica comercialmente el plugin.
     */
    String name();

    /**
     * Guarda la version del plugin en cuestión.
     */
    String version();

    /**
     * Guarda una descripción breve del servidor.
     */
    String description();

    /**
     * Guarda la ruta al entrypoint del plugin.
     */
    String classpath();

    /**
     * Guarda una lista separada por comas todos los autores del proyecto.
     */
    String[] authors();

    /**
     * Guarda una lista separada por comas de todos los colaboradores del proyecto.
     */
    String[] contributors();

    /**
     * Guarda una lista separada por comas de los sitios web de los autores.
     */
    String[] authorURLs();

    /**
     * Guarda una lista separada por comas de los sitios web de los colaboradores.
     */
    String[] contributorURLs();

    /**
     * Guarda la dirección web al sitio del proyecto.
     */
    String websiteURL();

    /**
     * Guarda la dirección web al sitio de la licencia del proyecto.
     */
    String licenseURL();

    /**
     * Guarda el prefijo que utilizaran las trazas de logging para identificarse.
     */
    String loggingPrefix();
}
