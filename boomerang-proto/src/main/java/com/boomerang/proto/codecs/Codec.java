package com.boomerang.proto.codecs;

import com.boomerang.proto.Identified;
import com.boomerang.proto.packets.Packet;
import com.boomerang.proto.packets.server.StatusRequestPacket;
import com.boomerang.proto.types.Type;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Codec<P extends Packet> extends Type<P>, Identified {

    void write(@NotNull ByteBuf buffer, P packet) throws IOException;

    P read(@NotNull ByteBuf buffer) throws IOException;

    @FunctionalInterface
    interface Encoder<P, R> extends Function<P, R> {
    }

    @FunctionalInterface
    interface Decoder<P, R> extends Function<P, R> {
    }

    static <R extends Packet> Codec<R> template(final R value, final int id) {
        return new Codec<>() {
            @Override
            public void write(@NotNull ByteBuf buffer, R value) throws IOException {
            }

            @Override
            public R read(@NotNull ByteBuf buffer) throws IOException {
                return value;
            }

            @Override
            public int id() {
                return id;
            }
        };
    }

    static <R extends Packet> Codec<R> template(final Supplier<R> supplier, int id) {
        return template(supplier.get(), id);
    }

    static <R extends Packet, P> Codec<R> template(Type<P> primitive, Decoder<R, P> deco, Encoder<P, R> enco, int id) {
        return new Codec<>() {
            @Override
            public void write(@NotNull ByteBuf buffer, R value) throws IOException {
                primitive.write(buffer, deco.apply(value));
            }

            @Override
            public R read(@NotNull ByteBuf buffer) throws IOException {
                return enco.apply(primitive.read(buffer));
            }

            @Override
            public int id() {
                return id;
            }
        };
    }
}
