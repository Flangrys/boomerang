package com.boomerang.proto.types;

import com.boomerang.proto.exceptions.CodecException;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class Numeric {

    public static final Type<Byte> BYTE = new Type<>() {

        @Override
        public void write(@NotNull ByteBuf buffer, Byte value) throws IOException {
            buffer.ensureWritable(1);
            buffer.writeByte(value);
        }

        @Override
        public Byte read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(1)) {
                throw new CodecException("There is not enough length to read a byte");
            }

            return buffer.readByte();
        }
    };

    public static final Type<Integer> VARINT = new Type<>() {
        public static final int VARINT_SIZE = 5;
        public static final int VARINT_SEGMENT_MASK = 0x7F;
        public static final int VARINT_CONTINUE_MASK = 0x80;

        @Override
        public void write(@NotNull ByteBuf buffer, Integer value) throws IOException {
            buffer.ensureWritable(VARINT_SIZE);

            while ((value & 0xFFFFFF80) != 0L) {
                int segmentedValue = (value & VARINT_SEGMENT_MASK) | VARINT_CONTINUE_MASK;

                buffer.writeByte(segmentedValue);

                value >>>= 7;
            }

            int continuedValue = value & VARINT_SEGMENT_MASK;
            buffer.writeByte(continuedValue);
        }

        @Override
        public Integer read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(1)) {
                throw new CodecException("There is not enough bytes to read in the current buffer", null);
            }

            int value = 0;
            int position = 0;
            byte current;

            while (((current = buffer.readByte()) & VARINT_CONTINUE_MASK) != 0L) {
                value |= (current & VARINT_SEGMENT_MASK) << position;

                position += 7;

                if (position > 35) {
                    throw new CodecException("The length of this varint is too long");
                }
            }

            return value | (current << position);
        }
    };

    public static final Type<Long> VARLONG = new Type<>() {
        public static final int VARLONG_SIZE = 5;
        public static final int VARLONG_SEGMENT_MASK = 0x7F;
        public static final int VARLONG_CONTINUE_MASK = 0x80;

        @Override
        public void write(@NotNull ByteBuf buffer, Long value) throws IOException {
            buffer.ensureWritable(VARLONG_SIZE);

            while ((value & 0xFFFFFF80L) != 0L) {
                int segmentedValue = (int) (value & VARLONG_SEGMENT_MASK) | VARLONG_CONTINUE_MASK;

                buffer.writeByte(segmentedValue);

                value >>>= 7;
            }

            int continuedValue = (int) (value & VARLONG_CONTINUE_MASK);
            buffer.writeByte(continuedValue);
        }

        @Override
        public Long read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(1)) {
                throw new CodecException("There is not enough bytes to read in the current buffer", null);
            }

            long value = 0;
            int position = 0;
            byte current;

            while (((current = buffer.readByte()) & VARLONG_SEGMENT_MASK) != 0L) {
                value |= (long) (current & VARLONG_SEGMENT_MASK) << position;

                position += 7;

                if (position > 35) {
                    throw new CodecException("The length of this varlong is too long");
                }
            }

            return value | ((long) current << position);
        }
    };

    public static final Type<Boolean> BOOLEAN = new Type<>() {
        public static final int TRUE = 0x1;
        public static final int FALSE = 0x0;

        @Override
        public void write(@NotNull ByteBuf buffer, Boolean value) throws IOException {
            buffer.ensureWritable(1);
            buffer.writeByte(value ? TRUE : FALSE);
        }

        @Override
        public Boolean read(@NotNull ByteBuf buffer) throws IOException {
            return switch (buffer.readByte()) {
                case TRUE -> true;
                case FALSE -> false;

                default -> throw new CodecException("Cannot read a boolean from an arbitrary value");
            };
        }
    };

    public static final Type<Long> LONG = new Type<>() {
        public static final int LONG_SIZE = 8;

        @Override
        public void write(@NotNull ByteBuf buffer, Long value) throws IOException {
            buffer.ensureWritable(LONG_SIZE);
            buffer.writeLong(value);
        }

        @Override
        public Long read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(LONG_SIZE)) {
                throw new CodecException("There is not enough bytes to read a long in the current buffer");
            }

            return buffer.readLong();
        }
    };

    public static final Type<Float> FLOAT = new Type<>() {
        public static final int FLOAT_SIZE = 4;

        @Override
        public void write(@NotNull ByteBuf buffer, Float value) throws IOException {
            buffer.ensureWritable(FLOAT_SIZE);
            buffer.writeFloat(value);
        }

        @Override
        public Float read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(FLOAT_SIZE)) {
                throw new CodecException("There is not enough bytes to read a float in the current buffer");
            }

            return buffer.readFloat();
        }
    };

    public static final Type<Double> DOUBLE = new Type<>() {
        public static final int DOUBLE_SIZE = 8;

        @Override
        public void write(@NotNull ByteBuf buffer, Double value) throws IOException {
            buffer.ensureWritable(DOUBLE_SIZE);
            buffer.writeDouble(value);
        }

        @Override
        public Double read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(DOUBLE_SIZE)) {
                throw new CodecException("There is not enough bytes to read a double in the current buffer");
            }

            return buffer.readDouble();
        }
    };

    public static final Type<Short> SHORT = new Type<>() {
        public static final int SHORT_SIZE = 2;

        @Override
        public void write(@NotNull ByteBuf buffer, Short value) throws IOException {
            buffer.ensureWritable(SHORT_SIZE);
            buffer.writeShort(value);
        }

        @Override
        public Short read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(SHORT_SIZE)) {
                throw new CodecException("There is not enough bytes to read a short in the current buffer");
            }

            return buffer.readShort();
        }
    };

    public static final Type<Integer> USHORT = new Type<>() {
        public static final int USHORT_SIZE = 2;

        @Override
        public void write(@NotNull ByteBuf buffer, Integer value) throws IOException {
            buffer.ensureWritable(USHORT_SIZE);
            buffer.writeShort(value & 0xFFFF);
        }

        @Override
        public Integer read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(USHORT_SIZE)) {
                throw new CodecException("There is not enough bytes to read a short in the current buffer");
            }

            return buffer.readUnsignedShort();
        }
    };
    public static final Type<Short> UBYTE = new Type<>() {
        @Override
        public void write(@NotNull ByteBuf buffer, Short value) throws IOException {
            buffer.ensureWritable(2);
            buffer.writeByte(value &  0xFF);
        }

        @Override
        public Short read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(2)) {
                throw new CodecException("There is not enough bytes to read a unsign byte in the current buffer");
            }

            return buffer.readUnsignedByte();
        }
    };
}
