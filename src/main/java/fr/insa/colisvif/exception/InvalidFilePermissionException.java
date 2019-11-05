package fr.insa.colisvif.exception;

import java.io.IOException;

public class InvalidFilePermissionException extends IOException {

    public InvalidFilePermissionException() {
    }

    public InvalidFilePermissionException(String message) {
        super(message);
    }

    public InvalidFilePermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
