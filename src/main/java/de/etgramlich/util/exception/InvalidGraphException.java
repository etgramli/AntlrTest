package de.etgramlich.util.exception;

public final class InvalidGraphException extends RuntimeException {
    /**
     * Creates new InvalidGraphException with the provided message.
     * @param message String, should not be empty.
     */
    public InvalidGraphException(final String message) {
        super(message);
    }
}
