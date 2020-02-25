package de.etgramlich.util.exception;

public final class UnrecognizedElementException extends RuntimeException {
    /**
     * Creates a new UnrecognizedElementException with the provided message.
     * @param message String, should not be empty.
     */
    public UnrecognizedElementException(final String message) {
        super(message);
    }
}
