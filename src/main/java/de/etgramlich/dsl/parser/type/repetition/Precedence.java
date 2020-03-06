package de.etgramlich.dsl.parser.type.repetition;

import de.etgramlich.dsl.parser.type.Alternatives;

/**
 * Represents a precedence from BNF as Java type.
 */
public final class Precedence extends AbstractRepetition {
    /**
     * Creates new Precedence from alternatives.
     * @param alternatives Alternatives, must not be null.
     */
    public Precedence(final Alternatives alternatives) {
        super(alternatives);
    }
}
