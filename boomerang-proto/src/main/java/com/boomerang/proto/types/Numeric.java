package com.boomerang.proto.types;

import com.boomerang.proto.exceptions.CodecException;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class Numeric {
    public static final int VARNUM_SIZE = 5, VARNUM_SEGMENT_MASK = 0x7F, VARNUM_CONTINUE_MASK = 0x80;

    public static boolean hasContinuationMask(int value) {
        return (value & VARNUM_CONTINUE_MASK) == VARNUM_SEGMENT_MASK;
    }

    public static final Type<Byte> BYTE = new Type<>() {
        @Override
        public void write(@NotNull ByteBuf buffer, Byte value) throws IOException {
            buffer.ensureWritable(1);
            buffer.writeByte(value);
        }

        @Override
        public Byte read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(1)) {
                throw new CodecException("There is not enough length to read namespace byte");
            }

            return buffer.readByte();
        }
    };

    public static final Type<Integer> VARINT = new Type<>() {
        @Override
        public void write(@NotNull ByteBuf buffer, Integer value) throws IOException {
            buffer.ensureWritable(VARNUM_SIZE);

            while ((value & ~VARNUM_CONTINUE_MASK) != 0) {
                int segmentedValue = (value & VARNUM_SEGMENT_MASK) | VARNUM_CONTINUE_MASK;

                buffer.writeByte(segmentedValue);

                value >>>= 7;
            }

            int continuedValue = value & VARNUM_SEGMENT_MASK;
            buffer.writeByte(continuedValue);
        }

        @Override
        public Integer read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(1)) {
                throw new CodecException("There is not enough bytes to read in the current buffer " + buffer);
            }

            int value = 0;
            int bytesRead = 0, shift = 0;
            boolean keepReading = true;

            while (bytesRead < 5 && keepReading) {
                if (!buffer.isReadable()) {
                    keepReading = false;

                } else {
                    byte current = buffer.readByte();
                    value |= (current & VARNUM_SEGMENT_MASK) << shift;
                    shift += 7;
                    bytesRead++;
                    keepReading = (current & VARNUM_CONTINUE_MASK) != 0;
                }
            }

            if (keepReading && bytesRead == 5) {
                throw new CodecException("VarInt too long");
            }

            if (!keepReading && bytesRead == 0) {
                throw new IndexOutOfBoundsException("VarInt too short");
            }

            return value;
        }
    };

    public static final Type<Long> VARLONG = new Type<>() {
        @Override
        public void write(@NotNull ByteBuf buffer, Long value) throws IOException {
            buffer.ensureWritable(VARNUM_SIZE);

            while ((value & ~VARNUM_CONTINUE_MASK) != 0) {
                int segmentedValue = (int) (value & VARNUM_SEGMENT_MASK) | VARNUM_CONTINUE_MASK;

                buffer.writeByte(segmentedValue);

                value >>>= 7;
            }

            int continuedValue = (int) (value & VARNUM_CONTINUE_MASK);
            buffer.writeByte(continuedValue);
        }

        @Override
        public Long read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(1)) {
                throw new CodecException("There is not enough bytes to read in the current buffer " + buffer);
            }

            long value = 0;

            int bytesRead = 0, shift = 0;
            boolean keepReading = true;

            while (bytesRead < 5 && keepReading) {
                if (buffer.isReadable()) {
                    byte current = buffer.readByte();

                    value |= (long) (current & VARNUM_SEGMENT_MASK) << shift;
                    shift += 7;
                    bytesRead++;
                    keepReading = (current & VARNUM_CONTINUE_MASK) != 0;

                } else {
                    keepReading = false;
                }
            }

            if (keepReading && bytesRead == 5) {
                throw new CodecException("VarLong too long");
            }

            if (!keepReading && bytesRead == 0) {
                throw new IndexOutOfBoundsException("VarLong too short");
            }

            return value;
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

                default -> throw new CodecException("Cannot read boolean from an arbitrary value");
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
                throw new CodecException("There is not enough bytes to read long in the current buffer");
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
                throw new CodecException("There is not enough bytes to read float in the current buffer");
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
                throw new CodecException("There is not enough bytes to read double in the current buffer");
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
                throw new CodecException("There is not enough bytes to read short in the current buffer");
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
                throw new CodecException("There is not enough bytes to read short in the current buffer");
            }

            return buffer.readUnsignedShort();
        }
    };
    public static final Type<Short> UBYTE = new Type<>() {
        @Override
        public void write(@NotNull ByteBuf buffer, Short value) throws IOException {
            buffer.ensureWritable(2);
            buffer.writeByte(value & 0xFF);
        }

        @Override
        public Short read(@NotNull ByteBuf buffer) throws IOException {
            if (!buffer.isReadable(2)) {
                throw new CodecException("There is not enough bytes to read unsign byte in the current buffer");
            }

            return buffer.readUnsignedByte();
        }
    };
}
