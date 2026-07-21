package com.boomerang.proto;

import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Los Resource Locators o {@link Namespace} son un tipo de identificador que permite distingir recursos contenidos
 * en un mismo grupo. Todos los Namespaces estan formados por un dominio y un path donde el dominio es simplemente el
 * nombre del grupo padre que contiene estos recursos, mientras que el path suele verse como la ruta de un directorio
 * o simplemente un nombre.
 *
 * <h2>Spec</h2>
 * Formalmente un {@link Namespace} se define como una secuencia de caracteres alfanumericos, separados por puntos,
 * guines medios, giones bajos y barras curvas, este ultimo siendo esclusivo del path. Separados por un ':' de modo
 * que a la izquierda se encuentre el dominio y del lado derecho el path del recurso.
 * <pre>
 * <code>
 *  word         := [a-z0-9]+
 *  domain       := word(?:[._-]+word)*
 *  path         := word(?:[._/-]+word)*
 *  namespace    := domain:path
 * </code>
 * </pre>
 *
 * @see <a href="https://www.minecraft.net/en-us/article/minecraft-snapshot-17w43a">Snapshot 1.13</a>
 */
public record Namespace(
        @Pattern(NAMESPACE_DOMAIN) String domain,
        @Pattern(NAMESPACE_PATH) String path
) {

    public static final char NAMESPACE_TAG = '#';

    public static final String NAMESPACE_DIVIDER = ":";

    @RegExp
    public static final String MINECRAFT = "minecraft";

    @RegExp
    public static final String BOOMERANG = "boomerang";


    @RegExp
    public static final String NAMESPACE_WORD = "[a-z0-9]+";

    @RegExp
    public static final String NAMESPACE_DOMAIN = "#?[a-z0-9]+";

    @RegExp
    public static final String NAMESPACE_PATH = NAMESPACE_WORD + "(?:[._/-]+[a-z0-9]+)*";

    @RegExp
    public static final String NAMESPACE_REGEX = NAMESPACE_DOMAIN + ":" + NAMESPACE_PATH;

    private static final java.util.regex.Pattern NAMESPACE_COMPILED_PATTERN = java.util.regex.Pattern.compile(NAMESPACE_REGEX);

    /**
     * Prueba que la secuencia dada cumpla parcialmente con la forma del {@link Namespace}. Notar que esta operación
     * no garantiza estrictamente la forma del namespace pudiendo resultar en falsos positivos.
     *
     * <p> Esta prueba utiliza captura solamente quellos caracteres legales en la secuencia pero no valida la
     * morfología. A diferencia de {@link #testFullNamespaceRegex(String)} que captura un unico namespace.
     *
     * <p>Esta opcion esta diseñada para escenarios simples, donde se precisa velocidad antes que la seguridad, Sin
     * embargo esto puede producir que el sistema falle cuando menos se espere. En estos casos recomendamos
     * encarecidamente utilizar test unitaros y end-to-end para validar que se esta produciendo y consumiendo
     * correctamente.
     *
     * @param namespace Una secuencia de caracteres, potencialmente un namespace.
     * @return {@code true} Si y solo si la totalidad de la secuencia corresponde con un namespace.
     */
    public static boolean testAnyNamespaceRegex(String namespace) {
        return namespace.chars().allMatch(c -> {
            return Character.isDigit(c) || (Character.isLetter(c) && Character.isLowerCase(c))
                    || c == '#'
                    || c == ':'
                    || c == '-'
                    || c == '_'
                    || c == '.'
                    || c == '/';
        });
    }

    /**
     * Prueba que la secuencia dada cumpla estrictamente con la expresion regular formal del {@link Namespace}. Notar
     * que esta operacion es sustancialmente costosa a costa de garantizar la integridad de una secuencia aleatoria.
     *
     * <p>Esta prueba utiliza una expresion regular fuerte que captura la totalidad del namespace pudiendo garantizar
     * que algunos edge-cases de {@link #testAnyNamespaceRegex(String)} puede omitir.
     *
     * <p>Esta opción fue diseñada para escenarios concretos donde se precisa garantizar al máximo que un namespace
     * sea totalmente valido. Sin embargo el coste de las expresiones regulares no justifica utilizarlo en todos los
     * escenarios, especialmente en la etapa de red.
     *
     * @param namespace Una secuencia de caracteres, potencialmente un namespace.
     * @return {@code true} Si y solo si la totalidad de la secuencia corresponde con un namespace.
     */
    public static boolean testFullNamespaceRegex(String namespace) {
        return NAMESPACE_COMPILED_PATTERN.matcher(namespace).matches();
    }

    /**
     * Construye un {@link Namespace} dado un dominio y una ruta, uniendo ambas unidades con
     * {@link Namespace#NAMESPACE_DIVIDER} y sin validar la morfología del namespace producido.
     *
     * <p>Este constructor esta pensado escenarios donde las garantias de que cualquier secuencia aleatoria de
     * caracteres sea un {@link Namespace} sean del cien por cien. Recomendamos utilizar este constructor solamente
     * cuando sea estrictamente necesario hardcodear un namespace.
     *
     * @param domain El identificador del grupo al que corresponde el recurso.
     * @param path   La ruta que localiza al recurso en este dominio.
     * @see #fromString(String)
     * @see #fromStringStrict(String)
     * @see #fromMinecraft(String)
     * @see #fromBoomerang(String)
     */
    public Namespace {
        if (domain == null || domain.isBlank()) {
            throw new IllegalArgumentException("Namespaces should start with namespace valid domain");
        }

        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Namespaces should start with namespace valid path");
        }

        domain = domain.toLowerCase(Locale.ENGLISH);
        path = path.toLowerCase(Locale.ENGLISH);
    }

    public boolean isTag() {
        return this.domain.charAt(0) == NAMESPACE_TAG;
    }

    /**
     * Devuelve este namespace en forma de {@link String} sin importar si la secuencia formada es valida.
     *
     * @return Una cadena de texto con la forma {@code minecraft:cobblestone}
     */
    @Override
    @NotNull
    public String toString() {
        return domain + ":" + path;
    }

    /**
     * Construye un {@link Namespace} a partir de una cadena de texto validando fuertemente la correctitud de la
     * cadena. Para validar la correctitud se utiliza {@link #testFullNamespaceRegex(String)}
     *
     * @param maybeNamespace Una cadena de texto potencialmente un namespace.
     * @return Una nueva instancia de {@link Namespace} si y solo si la cadena dada es correcta.
     * @throws IllegalArgumentException Cuando se provea null o la cadena de texto sea invalida.
     */
    public static Namespace fromStringStrict(String maybeNamespace) {
        if (maybeNamespace == null || maybeNamespace.isBlank()) {
            throw new IllegalArgumentException("A namespace-like sequence must be provided");
        }

        final boolean isNamespaceValid = testFullNamespaceRegex(maybeNamespace);

        if (!isNamespaceValid) {
            throw new IllegalArgumentException("An invalid namespace were provided: " + maybeNamespace);
        }

        final String[] namespace = maybeNamespace.split(NAMESPACE_DIVIDER);

        if (namespace.length != 2) {
            throw new IllegalArgumentException("Could not parse the provided namespace: " + maybeNamespace);
        }

        return new Namespace(namespace[0], namespace[1]);
    }


    /**
     * Construye un {@link Namespace} a partir de una cadena de texto validando debilmente la correctitud de la
     * cadena. Para validar la correctitud se utiliza {@link #testAnyNamespaceRegex(String)}
     *
     * @param maybeNamespace Una cadena de texto potencialmente un namespace.
     * @return Una nueva instancia de {@link Namespace} si no contiene caracteres ilegales.
     * @throws IllegalArgumentException Cuando se provea null o la cadena de texto sea invalida.
     */
    public static Namespace fromString(String maybeNamespace) {
        final boolean isNamespaceValid = testAnyNamespaceRegex(maybeNamespace);

        if (!isNamespaceValid) {
            throw new IllegalArgumentException("An invalid namespace were provided: " + maybeNamespace);
        }

        final String[] namespace = maybeNamespace.split(NAMESPACE_DIVIDER);

        if (namespace.length != 2) {
            throw new IllegalArgumentException("Could not parse the provided namespace: " + maybeNamespace);
        }

        return new Namespace(namespace[0], namespace[1]);
    }

    /**
     * Construye un {@link Namespace} ligado al dominio {@link Namespace#MINECRAFT} y dado el path del recurso.
     * Este metodo no valida la correctitud del recurso recomendamos utilizar un metodo de validacion previo para
     * evitar futuros inconvenientes.
     *
     * @param resource El recurso que se trata identificar en este dominio.
     * @return Una nueva instancia de {@link Namespace}.
     */
    public static Namespace fromMinecraft(@Pattern(NAMESPACE_PATH) String resource) {
        return new Namespace(MINECRAFT, resource);
    }

    /**
     * Construye un {@link Namespace} ligado al dominio {@link Namespace#BOOMERANG} y dado el path del recurso.
     * Este metodo no valida la correctitud del recurso recomendamos utilizar un metodo de validacion previo para
     * evitar futuros inconvenientes.
     *
     * @param resource El recurso que se trata identificar en este dominio.
     * @return Una nueva instancia de {@link Namespace}.
     */
    public static Namespace fromBoomerang(@Pattern(NAMESPACE_PATH) String resource) {
        return new Namespace(BOOMERANG, resource);
    }
}
