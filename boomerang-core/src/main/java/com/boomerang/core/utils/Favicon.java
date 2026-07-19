package com.boomerang.core.utils;

import com.boomerang.core.BoomerangServer;
import com.boomerang.core.exceptions.InvalidResourceException;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Favicon {
    public static String loadFavicon(String path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("A path must be provided");
        }

        final URL faviconFile = BoomerangServer.class.getResource(path);

        if (faviconFile == null) {
            throw new InvalidResourceException("No favicon were found with the given path: " + path);
        }

        final var faviconImage = ImageIO.read(faviconFile);

        final int faviconHeight = faviconImage.getHeight();
        final int faviconWidth = faviconImage.getWidth();

        if (faviconWidth != 64 && faviconHeight != 64) {
            throw new InvalidResourceException("A server favicon image must be 64x64");
        }

        final var faviconEncoder = Base64.getEncoder();
        final var faviconStream = new ByteArrayOutputStream();

        ImageIO.write(faviconImage, "png", faviconStream);

        final var faviconEncoded = faviconEncoder.encode(faviconStream.toByteArray());

        return new String(faviconEncoded, StandardCharsets.UTF_8);
    }
}
