package com.boomerang.proto;

/**
 * Representa los recursos del juego que cuentan con un identificador alfanumerico notablemente superior a los
 * identificadores numericos, que permiten identificar múy facilmente un recurso de otro y evitar ambiguedades. Estos
 * fueron introducidos en la version {@code 1.13} durante una snapshot, evolucionando el mecanismo para identificar
 * recursos del juego.
 *
 * <p>Recomendamos encarecidamente utilizar namespaces en vez de identificadores en casos particulares, sin embargo
 * existen contextos donde se necesita ambos, por ejemplo en el protocolo de red donde se siguen utilizando
 * identificadores numericos al mismo tiempo que el registro de paquetes utiliza namespces.
 */
public interface Namespaced {

    Namespace namespace();
}
