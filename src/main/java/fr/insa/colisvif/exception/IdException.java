package fr.insa.colisvif.exception;

import fr.insa.colisvif.model.Node;

/**
 * An {@link Exception} thrown when a problem occurs with
 * {@link Node} IDs.
 */
public class IdException extends XMLException {

    /**
     * Creates a new {@link IdException} with the given message and no cause.
     * @param message the message to display with the {@link IdException}
     */
    public IdException(String message) {
        super(message);
    }
}
