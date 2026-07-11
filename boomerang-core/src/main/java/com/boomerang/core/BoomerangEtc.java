package com.boomerang.core;

import com.boomerang.proto.Namespace;

import java.util.Properties;

/**
 * {@link BoomerangEtc} Representa una base de datos clave-valor utilizada para almacenar en memoria, objetos y
 * recursos del juego en tiempo real.
 *
 * <p></p>
 * Esta base de datos aloja objetos de toda clase como: texturas, sonidos, particulas, bloques, estructuras, etc;
 * justo en memoria, permitiendo al servidor acceder de forma muy rapida a estos sin necesidad de bloquearse para
 * leer desde disco.
 *
 * <p></p>
 * Esta implementacion emplea {@link Namespace} para identificar los recursos, por medio del hash que se autogenera
 * este nombre de espacio.
 */
public final class BoomerangEtc {

    private final SnapshotManager<Properties> propertiesSnapshotManager = new SnapshotManager<>();

    /**
     * Devuelve la snapshot más reciente del archivo de configuración del servidor.
     * Si deseas obtener una copia fiel o actualizada, deberas utilizar {@link #loadProperties()}
     *
     * @return Una snapshot del archivo `server.properties` mas reciente.
     */
    public SnapshotAble<Properties> getProperties() {
        return null;
    }


    public SnapshotAble<Properties> loadProperties() {return null;}
}

