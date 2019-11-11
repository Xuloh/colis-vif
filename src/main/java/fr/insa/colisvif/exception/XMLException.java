package fr.insa.colisvif.exception;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.DeliveryMap;

/**
 * An {@link Exception} thrown when a {@link CityMap}
 * or {@link DeliveryMap} XML document is not valid.
 */
public class XMLException extends Exception {

    /**
     * Creates a new {@link XMLException} with the given message and no cause.
     * @param message the message to display with the {@link XMLException}
     */
    public XMLException(String message) {
        super(message);
    }

}
