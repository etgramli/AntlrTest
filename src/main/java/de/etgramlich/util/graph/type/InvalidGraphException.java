package de.etgramlich.util.graph.type;

public final class InvalidGraphException extends RuntimeException {
    public InvalidGraphException() {}

    public InvalidGraphException(final String message) {
        super(message);
    }
}
