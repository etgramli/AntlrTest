package de.etgramlich.parser.type.repetition;

import de.etgramlich.parser.type.Alternatives;

public final class ZeroOrMore extends AbstractRepetition {
    /**
     * Creates a new loop from alternatives.
     * @param alternatives Alternatives, must not be null.
     */
    public ZeroOrMore(final Alternatives alternatives) {
        super(alternatives);
    }

    @Override
    public boolean isRepetition() {
        return true;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean isPrecedence() {
        return false;
    }
}
