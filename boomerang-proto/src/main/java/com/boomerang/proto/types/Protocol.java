package com.boomerang.proto.types;

import com.boomerang.proto.ConnectionIntention;
import com.boomerang.proto.exceptions.CodecException;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class Protocol {

    public static final Type<ConnectionIntention> INTENTION = new Type<>() {
        @Override
        public void write(@NotNull ByteBuf buffer, ConnectionIntention value) throws IOException {
            Numeric.VARINT.write(buffer, value.intention());
        }

        @Override
        public ConnectionIntention read(@NotNull ByteBuf buffer) throws IOException {
            final int intentionOrdinal = Numeric.VARINT.read(buffer);
            final var intentionValue = ConnectionIntention.fromValue(intentionOrdinal);

            if (intentionValue == null) {
                throw new CodecException("Illegal intention value read: ", intentionOrdinal);

            } else {
                return intentionValue;
            }
        }
    };

    public static final Type<UUID> UUID = new Type<>() {
        @Override
        public void write(@NotNull ByteBuf buffer, UUID value) throws IOException {
            Numeric.LONG.write(buffer, value.getMostSignificantBits());
            Numeric.LONG.write(buffer, value.getLeastSignificantBits());
        }

        @Override
        public UUID read(@NotNull ByteBuf buffer) throws IOException {
            return new UUID(
                    Numeric.LONG.read(buffer),
                    Numeric.LONG.read(buffer)
            );
        }
    };

    public static final Type<ByteBuf> BUFFER = new Type<>() {
        @Override
        public void write(@NotNull ByteBuf buffer, ByteBuf value) throws IOException {
            buffer.writeBytes(value);
        }

        @Override
        public ByteBuf read(@NotNull ByteBuf buffer) throws IOException {
            return buffer.copy();
        }
    };

    public static <S> Type<Optional<S>> optional(Type<S> type) {
        return new Type<Optional<S>>() {
            @Override
            public void write(@NotNull ByteBuf buffer, Optional<S> value) throws IOException {
                final boolean isPresent = value.isPresent();

                Numeric.BOOLEAN.write(buffer, isPresent);

                if (isPresent) {
                    type.write(buffer, value.get());
                }
            }

            @Override
            public Optional<S> read(@NotNull ByteBuf buffer) throws IOException {
                final boolean isPresent = Numeric.BOOLEAN.read(buffer);

                if (isPresent) {
                    return Optional.of(type.read(buffer));

                } else {
                    return Optional.empty();
                }
            }
        };
    }

    public static <S> Type<List<S>> list(Type<S> type, int maxSize) {
        return new Type<List<S>>() {

            @Override
            public void write(@NotNull ByteBuf buffer, List<S> value) throws IOException {
                if (value == null) {
                    Numeric.VARINT.write(buffer, 0);

                } else {
                    Numeric.VARINT.write(buffer, value.size());

                    for (S item : value) {
                        type.write(buffer, item);
                    }
                }
            }

            @Override
            public List<S> read(@NotNull ByteBuf buffer) throws IOException {
                final int size = Numeric.VARINT.read(buffer);

                if (size > maxSize) {
                    throw new CodecException("Cannot read more bytes than the specified in the maxSize argument");

                } else {
                    final List<S> value = new ArrayList<>(size);

                    for (int i = 0; i < size; i++) {
                        value.add(type.read(buffer));
                    }

                    return value;
                }
            }
        };
    }

    public static <S> Type<List<S>> list(Type<S> type) {
        return list(type, Integer.MAX_VALUE);
    }
}
