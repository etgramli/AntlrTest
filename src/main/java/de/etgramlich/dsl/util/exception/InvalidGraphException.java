package de.etgramlich.dsl.util.exception;

/**
 * Exception indicating that something went wrong on graph creation / alteration.
 */
public final class InvalidGraphException extends RuntimeException {
    /**
     * Creates new InvalidGraphException with the provided message.
     * @param message String, should not be empty.
     */
    public InvalidGraphException(final String message) {
        super(message);
    }
}
