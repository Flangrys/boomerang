package com.boomerang.proto.packets.server;

import com.boomerang.proto.codecs.Codec;
import com.boomerang.proto.packets.ServerboundPacket;
import com.boomerang.proto.types.Numeric;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public record PingRequestPacket(
        int id,
        Long timestamp
) implements ServerboundPacket.Status, ServerboundPacket {
    public static final Codec<PingRequestPacket> CODEC = new Codec<PingRequestPacket>() {
        @Override
        public void write(@NotNull ByteBuf buffer, PingRequestPacket packet) throws IOException {
            Numeric.VARINT.write(buffer, packet.id());
            Numeric.LONG.write(buffer, packet.timestamp());
        }

        @Override
        public PingRequestPacket read(@NotNull ByteBuf buffer) throws IOException {
            final var id = Numeric.VARINT.read(buffer);
            final var timestamp = Numeric.LONG.read(buffer);
            return new PingRequestPacket(id, timestamp);
        }

        @Override
        public int id() {
            return 0x1;
        }
    };
}
