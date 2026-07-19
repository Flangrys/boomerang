package com.boomerang.core.exceptions;

import java.io.IOException;

public class InvalidResourceException extends IOException {
    public InvalidResourceException(String message) {
        super(message);
    }

    public InvalidResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
