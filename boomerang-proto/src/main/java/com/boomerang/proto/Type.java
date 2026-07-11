package com.boomerang.proto;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Esta interfaz representa la abstraccion primitiva de los datos que viajan a travez del protocolo de red de Minecraft.
 * Su objetivo es encapsular la logica de serializacion y deserializacion de los tipos primitivos (y compuestos) en
 * los buffers de red, aislando logica la logica de red de la logica del juego.
 *
 * @param <T> El tipo de dato que este procesa el codec.
 */
public interface Type<T> {

    /**
     * Serializa y escribe el valor proporcionado en el buffer de red.
     *
     * @param buffer El buffer de destino {@link ByteBuf} donde se escribirán los bytes. No debe ser nulo.
     * @param value  El valor de tipo {@link T} que se va a codificar.
     * @throws IOException Si ocurre un error de desbordamiento en el buffer o falla la escritura.
     */
    void write(@NotNull ByteBuf buffer, T value) throws IOException;


    /**
     * Similar a {@link #write(ByteBuf, T)} pero la escritura se limita a un tamaño maximo.
     *
     * @param buffer    El bufer de origen {@link ByteBuf} desde donde se leerán los bytes. No debe ser nulo.
     * @param maxLength El tamaño maximo admitido en la escritura.
     * @throws IOException Si el buffer no contiene suficientes bytes o los datos están corruptos.
     * @implNote La implementación por defecto ignora totalmente el parametro {@code maxLength}.
     */
    default void write(@NotNull ByteBuf buffer, T value, int maxLength) throws IOException {
        write(buffer, value);
    }

    /**
     * Lee e interpreta los bytes del buffer para reconstruir el objeto original.
     *
     * @param buffer El buffer de origen {@link ByteBuf} desde donde se leerán los bytes. No debe ser nulo.
     * @return El objeto deserializado de tipo {@link T}.
     * @throws IOException Si el buffer no contiene suficientes bytes o los datos están corruptos.
     */
    T read(@NotNull ByteBuf buffer) throws IOException;

    /**
     * Similar a {@link #read(ByteBuf)} pero la lectura se limita a un tamaño maximo.
     *
     * @param buffer    El bufer de origen {@link ByteBuf} desde donde se leerán los bytes. No debe ser nulo.
     * @param maxLength El tamaño maximo admitido en la lectura.
     * @return El objeto deserializado de tipo {@link T}.
     * @throws IOException Si el buffer no contiene suficientes bytes o los datos están corruptos.
     * @implNote La implementación por defecto ignora totalmente el parametro {@code maxLength}.
     */
    default T read(@NotNull ByteBuf buffer, int maxLength) throws IOException {
        return read(buffer);
    }

    /**
     * Transforma este {@link Type<T>} en un nuevo {@link Type<S>} aplicando funciones de conversión intermedias.
     *
     * <p></p>
     * Este metodo es de utilidad para reutilizar codecs primitivos existentes y adaptarlos a tipos de datos
     * más complejos o fuertemente tipados (como Enums o Value Objects).
     *
     * @param <S>          El nuevo tipo de dato que manejará el codec resultante.
     * @param deserializer Función que transforma el tipo nuevo {@link S} al tipo base {@link T} (se ejecuta durante la escritura).
     * @param serializer   Función que transforma el tipo base {@link T} al tipo nuevo {@link S} (se ejecuta durante la lectura).
     * @return Una nueva instancia de {@link Type<S>} que delega en este tipo base de manera transparente.
     */
    default <S> Type<S> transform(Function<S, T> deserializer, Function<T, S> serializer) {
        return new Type<S>() {
            @Override
            public void write(@NotNull ByteBuf buffer, S value) throws IOException {
                final var serializedValue = deserializer.apply(value);
                Type.this.write(buffer, serializedValue);
            }

            @Override
            public S read(@NotNull ByteBuf buffer) throws IOException {
                final var deserializedValue = Type.this.read(buffer);
                return serializer.apply(deserializedValue);
            }
        };
    }

    /**
     * Crea un codec delegado cuya inicialización real se pospone hasta que se invoca por primera vez
     * un metodo de lectura o escritura.
     *
     * <p></p>
     * <b>Caso de uso crítico:</b> Este metodo es fundamental para resolver dependencias circulares
     * durante la fase de inicialización del protocolo (por ejemplo, cuando un paquete o estructura de datos
     * requiere una referencia a sí misma o a otro tipo que aún no ha sido instanciado).
     *
     * @param <S>          El tipo de dato que maneja el codec diferido.
     * @param typeSupplier El proveedor {@link Supplier} que retornará la instancia real del {@link Type<S>} cuando
     *                     sea requerido.
     * @return Un {@link Type<S>} de evaluación perezosa (Lazy).
     */
    default <S> Type<S> lazyType(Supplier<Type<S>> typeSupplier) {
        return new Type<S>() {
            private Type<S> type = null;

            @Override
            public void write(@NotNull ByteBuf buffer, S value) throws IOException {
                if (type == null) type = typeSupplier.get();
                type.write(buffer, value);
            }

            @Override
            public S read(@NotNull ByteBuf buffer) throws IOException {
                if (type == null) type = typeSupplier.get();
                return type.read(buffer);
            }
        };
    }
}