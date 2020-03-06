package de.etgramlich.dsl.util.exception;

/**
 * Used to show that during parsing or graph creation an unknown / unexpected element occured.
 */
public final class UnrecognizedElementException extends RuntimeException {
    /**
     * Creates a new UnrecognizedElementException with the provided message.
     * @param message String, should not be empty.
     */
    public UnrecognizedElementException(final String message) {
        super(message);
    }
}
