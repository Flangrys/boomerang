package com.boomerang.proto.types;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Esta interfaz representa la abstraccion primitiva de los datos que viajan a travez del protocolo de red de Minecraft.
 * Su objetivo es encapsular la logica de serializacion y deserializacion de datos primitivos (y compuestos) en
 * los buffers de red aislandolo de la logica del juego.
 *
 * @param <T> El tipo de dato que este soporta el codec.
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
     * @param buffer    El bufer de origen {@link ByteBuf} desde donde se leerán los bytes.
     * @param maxLength El tamaño maximo admitido en la escritura.
     * @throws IOException Si el buffer no contiene suficientes bytes o los datos están corruptos.
     */
    default void write(@NotNull ByteBuf buffer, T value, int maxLength) throws IOException {
        write(buffer, value);
    }

    /**
     * Lee e interpreta los bytes del buffer para reconstruir el objeto original.
     *
     * @param buffer El buffer de origen {@link ByteBuf} desde donde se leerán los bytes.
     * @return El objeto deserializado de tipo {@link T}.
     * @throws IOException Si el buffer no contiene suficientes bytes o los datos están corruptos.
     */
    T read(@NotNull ByteBuf buffer) throws IOException;

    /**
     * Similar a {@link #read(ByteBuf)} pero la lectura se limita a un tamaño maximo.
     *
     * @param buffer    El bufer de origen {@link ByteBuf} desde donde se leerán los bytes.
     * @param maxLength El tamaño maximo admitido en la lectura.
     * @return El objeto deserializado de tipo {@link T}.
     * @throws IOException Si el buffer no contiene suficientes bytes o los datos están corruptos.
     */
    default T read(@NotNull ByteBuf buffer, int maxLength) throws IOException {
        return read(buffer);
    }

    /**
     * Transforma este {@link Type<T>} en un subtipo {@link Type<S>} por medio de metodos intermedios transformadores.
     *
     * @param <S>          El nuevo tipo de dato que manejará el codec resultante.
     * @param deserializer Función que codifica el tipo nuevo {@link S} al tipo base {@link T}.
     * @param serializer   Función que decodifica el tipo base {@link T} al tipo nuevo {@link S}
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
     * Transforma este {@link Type<T>} en un subtipo vago, es decir que el {@link Type<T>} se computa al vuelo.
     *
     * @param <S>          El tipo de dato que maneja el codec diferido.
     * @param typeSupplier El proveedor {@link Supplier} que retornará la instancia real del {@link Type<S>} cuando
     *                     sea requerido.
     * @return Un {@link Type<S>} computado de forma vaga.
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

    /**
     * Computa el tamaño del valor de {@link T} en un {@link ByteBuf}. Recomendamos utilizar cualquier derivado de
     * {@link ByteBuf} que sea {@code Pooled} y altamente disponible. Ademas es necesario considerar los riesgos de
     * contaminar un buffer de produccion o alojar constantemente un buffer ya que ambos escenarios afectan al
     * rendimiento.
     *
     * @param buffer Un buffer apto para escribir el dato.
     * @param value El valor cuyo tamaño se desea computar.
     * @return El tamaño del valor dado.
     * @throws IOException Si ocurre un error durante la escritura en el buffer.
     */
    default int size(@NotNull ByteBuf buffer, T value) throws IOException {
        try {
            buffer.markWriterIndex();

            this.write(buffer, value);

            return buffer.writerIndex() - buffer.readerIndex();

        } finally {
            buffer.resetWriterIndex();
        }
    }
}