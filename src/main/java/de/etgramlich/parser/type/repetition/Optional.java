package de.etgramlich.parser.type.repetition;

import de.etgramlich.parser.type.Alternatives;

/**
 * Represents an optional element from BNF as Java type.
 */
public final class Optional extends AbstractRepetition {
    /**
     * Creates new Optional object from Alternatives.
     * @param alternatives Alternatives, must not be null.
     */
    public Optional(final Alternatives alternatives) {
        super(alternatives);
        if (alternatives.getSequences().size() > 1) {
            throw new IllegalArgumentException("Optional argument has more than one Alternative! (must be 0 or 1)");
        }
    }
}
