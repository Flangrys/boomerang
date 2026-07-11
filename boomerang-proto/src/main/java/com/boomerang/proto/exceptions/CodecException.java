package com.boomerang.proto.exceptions;

import java.io.IOException;

public class CodecException extends IOException {
    public CodecException(String message) {
        super(message);
    }

    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodecException(String message, Object any) {
        super("%s: %s (%S)".formatted(message, any.toString(), any.getClass().toString()));
    }
}
