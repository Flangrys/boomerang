package com.boomerang.proto.packets.server;

import com.boomerang.proto.ConnectionIntention;
import com.boomerang.proto.codecs.Codec;
import com.boomerang.proto.exceptions.CodecException;
import com.boomerang.proto.packets.IntentionPacket;
import com.boomerang.proto.packets.ServerboundPacket;
import com.boomerang.proto.types.Numeric;
import com.boomerang.proto.types.Primitive;
import com.boomerang.proto.types.Protocol;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public record HandshakePacket(
        Integer protocolVersion,
        String serverAddress,
        Integer serverPort,
        ConnectionIntention intentions
) implements ServerboundPacket.Handshake, ServerboundPacket, IntentionPacket {

    public static final Codec<HandshakePacket> CODEC = new Codec<>() {
        @Override
        public void write(@NotNull ByteBuf buffer, HandshakePacket value) throws IOException {
            throw new CodecException("Handshake packets are not writable");
        }

        @Override
        public HandshakePacket read(@NotNull ByteBuf buffer) throws IOException {
            final var protocolVersion = Numeric.VARINT.read(buffer);
            final var serverAddress = Primitive.STRING.read(buffer);
            final var serverPort = Numeric.USHORT.read(buffer);
            final var intention = Protocol.INTENTION.read(buffer);

            return new HandshakePacket(protocolVersion, serverAddress, serverPort, intention);
        }

        public int id() {
            return 0x0;
        }
    };

    @Override
    public int id() {
        return 0x0;
    }
}
