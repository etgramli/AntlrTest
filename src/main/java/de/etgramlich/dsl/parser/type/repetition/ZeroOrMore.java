package de.etgramlich.dsl.parser.type.repetition;

import de.etgramlich.dsl.parser.type.Alternatives;

/**
 * Represents a zero-or-more-repetition from BNF as Java type.
 */
public final class ZeroOrMore extends AbstractRepetition {
    /**
     * Creates a new loop from alternatives.
     * @param alternatives Alternatives, must not be null.
     */
    public ZeroOrMore(final Alternatives alternatives) {
        super(alternatives);
    }
}
