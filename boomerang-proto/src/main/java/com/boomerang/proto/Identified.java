package com.boomerang.proto;

/**
 * Representa los recursos del juego que cuentan con un identificador numerico que los distingue de forma unica.
 * Estos identificadores aun siguen siendo utilizados por retrocompatibilidad y por el propio protocolo de red.
 *
 * <p>A diferencia de {@link Namespaced}, este mecanismo no garantiza la existencia de un unico id. En otras
 * palabras, pueden existir conflictos o ambiguedades. Por lo que recomendamos suma precaución al momento de utilizar
 * un identificador numerico.
 *
 * @see Namespaced
 */
public interface Identified {

    int id();
}
