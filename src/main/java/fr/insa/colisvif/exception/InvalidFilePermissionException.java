package fr.insa.colisvif.exception;

import java.io.File;
import java.io.IOException;

/**
 * An {@link Exception} thrown when a {@link File} doesn't have
 * the required permissions.
 */
public class InvalidFilePermissionException extends IOException {

    /**
     * Creates a new {@link InvalidFilePermissionException} with the no message
     * and no cause
     */
    public InvalidFilePermissionException() {
    }

    /**
     * Creates a new {@link InvalidFilePermissionException} with the given
     * message and no cause.
     * @param message the message to display with the
     * {@link InvalidFilePermissionException}
     */
    public InvalidFilePermissionException(String message) {
        super(message);
    }

    /**
     * Creates a new {@link InvalidFilePermissionException} with the given
     * message and cause.
     * @param message the message to display with the
     * {@link InvalidFilePermissionException}
     * @param cause the {@link Throwable} that caused
     *              this exception to be thrown
     */
    public InvalidFilePermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
