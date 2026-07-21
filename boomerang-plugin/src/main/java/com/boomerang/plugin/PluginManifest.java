package com.boomerang.plugin;

import com.boomerang.plugin.exceptions.PluginManifestException;
import com.boomerang.plugin.loader.PluginClassLoader;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * {@link PluginManifest} es un contenedor del archivo {@code manifest.properties} utilizado durante runtime para
 * cargar e inyectar un plugin al servidor.
 *
 * <p>BoomerangMC utiliza el manifiesto para gestionar los procesos de carga y descarga de plugins. Ya que cada uno
 * puede comportarse de distintas formas, es necesario declarar como el {@link ClassLoader} del propio plugin debera
 * comportarse para cargar y/o descargar este.
 *
 * @see Manifestable
 */
public record PluginManifest(
        String name,
        String version,
        String description,
        String classpath,
        String[] authors,
        String[] contributors,
        String[] authorURLs,
        String[] contributorURLs,
        String websiteURL,
        String licenseURL,
        String loggingPrefix
) implements Manifestable {

    private static final Configurations CONFIGURATIONS = new Configurations();

    public PluginManifest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Manifest property name is missing");
        }

        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Manifest property name is missing");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Manifest property name is missing");
        }

        if (classpath == null || classpath.isBlank()) {
            throw new IllegalArgumentException("Manifest property name is missing");
        }
    }

    /**
     * Lee el manifiesto dado un {@link File} e intentara volcarlo en una nueva instancia de {@link PluginManifest}.
     * Es necesario notar que si alguna propiedad en el archivo no se encuentra presente, su propiedad en la
     * instancia se marcara como nulo de la misma manera.
     *
     * @param manifestFile Un {@link File} que apunte al archivo de manifiesto del plugin.
     * @return Una instancia de {@link PluginManifest}.
     * @throws PluginManifestException Si ocurre un error durante la inicializacion de la configuracion.
     */
    public static PluginManifest readManifest(@NotNull File manifestFile) throws PluginManifestException {
        try {
            final PropertiesConfiguration config = CONFIGURATIONS.properties(manifestFile);
            config.setListDelimiterHandler(new DefaultListDelimiterHandler(','));

            return new PluginManifest(
                    config.getString("name"),
                    config.getString("version"),
                    config.getString("description"),
                    config.getString("classpath"),
                    config.getStringArray("authors"),
                    config.getStringArray("contributors"),
                    config.getStringArray("authorsURLs"),
                    config.getStringArray("contributorsURLs"),
                    config.getString("websiteURL"),
                    config.getString("licenseURL"),
                    config.getString("loggingPrefix")
            );
        } catch (ConfigurationException e) {
            throw new PluginManifestException("Cannot read manifest file: ", e);
        }
    }

    /**
     * Escribe la instancia de {@link PluginManifest} dada en el archivo manifiesto localizado en {@link File} dado.
     * Es necesario notar que si algun atributo esta marcada como null, la propiedad asociada no se volcara en el
     * archivo.
     *
     * @param manifestFile Un {@link File} que apunte al archivo de manifiesto del plugin.
     * @param manifest La instancia de {@link PluginManifest} que se desea volcar en el archivo.
     * @throws PluginManifestException Si ocurre un error durante la inicializacion de la configuracion.
     */
    public static void writeManifest(@NotNull File manifestFile, @NotNull PluginManifest manifest) throws PluginManifestException {
        try {
            final PropertiesConfiguration config = CONFIGURATIONS.properties(manifestFile);
            config.setListDelimiterHandler(new DefaultListDelimiterHandler(','));

            config.setProperty("name", manifest.name);
            config.setProperty("version", manifest.version);
            config.setProperty("description", manifest.description);
            config.setProperty("classpath", manifest.classpath);
            config.setProperty("authors", manifest.authors);
            config.setProperty("contributors", manifest.contributors);
            config.setProperty("authorURLs", manifest.authorURLs);
            config.setProperty("contributorURLs", manifest.contributorURLs);
            config.setProperty("websiteURL", manifest.websiteURL);
            config.setProperty("licenseURL", manifest.licenseURL);
            config.setProperty("loggingPrefix", manifest.loggingPrefix);

        } catch (ConfigurationException e) {
            throw new PluginManifestException("Cannot write manifest file: ", e);
        }
    }
}
