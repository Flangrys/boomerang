package com.boomerang.proto.packets.client;

import com.boomerang.proto.codecs.Codec;
import com.boomerang.proto.packets.ClientboundPacket;
import com.boomerang.proto.types.Primitive;
import com.boomerang.proto.types.Type;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public record StatusResponsePacket(String json) implements ClientboundPacket.Status, ClientboundPacket {
    public static final String DEFAULT_SERVER_STATUS_FILE_NAME = "/server_status.json";

    public static Codec<StatusResponsePacket> CODEC = Codec.template(
            Primitive.STRING, StatusResponsePacket::json, StatusResponsePacket::new, 0x0
    );

    @Override
    public int id() {
        return 0x0;
    }

    public static StatusResponsePacket fromDefaultServerStatus() throws IOException {
        final var fileStream = StatusResponsePacket.class.getResourceAsStream(DEFAULT_SERVER_STATUS_FILE_NAME);

        if (fileStream == null) {
            throw new FileNotFoundException(DEFAULT_SERVER_STATUS_FILE_NAME);
        }

        final StringBuilder builder = new StringBuilder();

        try (final var streamReader = new InputStreamReader(fileStream)) {
            try (final var bufferReader = new BufferedReader(streamReader)) {
                String line;

                while ((line = bufferReader.readLine()) != null) {
                    builder.append(line.trim());
                }

                return new StatusResponsePacket(builder.toString());
            }
        }
    }
}
