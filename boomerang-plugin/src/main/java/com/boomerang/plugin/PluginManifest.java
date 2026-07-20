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
 * {@link PluginManifest} es un contenedor del archivo <code>manifest.properties</code> utilizado durante runtime para
 * cargar e inyectar un plugin al servidor.
 *
 * <p> Cuando BoomerangMC inicia el proceso de carga de plugins, {@link PluginClassLoader} intenta localizar el
 * manifiesto y consigo traerse el campo <code>classpath</code> que identifica el punto de entrada del plugin. Este
 * punto de entrada del plugin permite
 * </p>
 *
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
