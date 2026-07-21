package com.boomerang.plugin;


import com.boomerang.plugin.loader.PluginClassLoader;

/**
 * Este contrato representa el contenedor de información que replica al archivo de manifiesto
 * <code>manifest.properties</code>. La información que recauda es necesaria para que {@link PluginClassLoader} pueda
 * cargar e inyectar el plugin en el servidor.
 *
 * @see PluginManifest Para conocer como se obtiene esta información
 * @see PluginClassLoader Para conocer como se carga un plugin a partir del manifiesto.
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
