package com.boomerang.proto.types;

import com.boomerang.proto.exceptions.CodecException;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class Collection {

    public static final Type<byte[]> ARRAY = new Type<byte[]>() {

        @Override
        public void write(@NotNull ByteBuf buffer, byte[] value) throws IOException {
            buffer.ensureWritable(value.length);
            buffer.writeBytes(value);
        }

        @Override
        public void write(@NotNull ByteBuf buffer, byte[] value, int maxLength) throws IOException {
            if (maxLength == -1 || value.length <= maxLength) {
                this.write(buffer, value);

            } else {
                throw new CodecException("Cannot write more than" + maxLength + " bytes");
            }
        }

        @Override
        public byte[] read(@NotNull ByteBuf buffer) throws IOException {
            return this.read(buffer, -1);
        }

        @Override
        public byte[] read(@NotNull ByteBuf buffer, int maxLength) throws IOException {
            final int readable = buffer.readableBytes();
            byte[] array;

            if (maxLength == -1) {
                array = new byte[readable];
            } else {
                array = new byte[Math.min(maxLength, readable)];
            }

            buffer.readBytes(array);

            return array;
        }
    };

    public static final Type<byte[]> PREFIXED_ARRAY = new Type<byte[]>() {
        @Override
        public void write(@NotNull ByteBuf buffer, byte[] value) throws IOException {
            Numeric.VARINT.write(buffer, value.length);

            ARRAY.write(buffer, value);
        }

        @Override
        public byte[] read(@NotNull ByteBuf buffer) throws IOException {
            final int length = Numeric.VARINT.read(buffer);

            if (length == 0) return new byte[0];

            if (!buffer.isReadable(length)) {
                throw new CodecException("There is not enough bytes to be read");
            }

            return ARRAY.read(buffer, length);
        }
    };
}
