package com.boomerang.proto;

/**
 * {@link ConnectionBound} Representa los limites del protocolo sobre los paquetes de red del juego.
 * Estos limites permiten inferir en que estado se encuentra actualmente la conexion con el cliente.
 */
public enum ConnectionBound {
    CLIENTBOUND,
    SERVERBOUND;

    public ConnectionBound opposite() {
        return switch (this) {
            case CLIENTBOUND -> SERVERBOUND;
            case SERVERBOUND -> CLIENTBOUND;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case SERVERBOUND -> "SERVERBOUND";
            case CLIENTBOUND -> "CLIENTBOUND";
        };
    }
}
