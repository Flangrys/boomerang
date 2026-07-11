package com.boomerang.proto.types;

import com.boomerang.proto.Type;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class Primitive {

    public static final Type<String> STRING = new Type<String>() {
        @Override
        public void write(@NotNull ByteBuf buffer, String value) throws IOException {
            final byte [] bytes = value.getBytes(StandardCharsets.UTF_8);

            Collection.PREFIXED_ARRAY.write(buffer, bytes);
        }

        @Override
        public String read(@NotNull ByteBuf buffer) throws IOException {
            final byte[] bytes = Collection.PREFIXED_ARRAY.read(buffer);

            return new String(bytes, StandardCharsets.UTF_8);
        }
    };
}
