package de.etgramlich.parser.type.repetition;

import de.etgramlich.parser.type.Alternatives;

public final class Precedence extends AbstractRepetition {
    /**
     * Creates new Precedence from alternatives.
     * @param alternatives Alternatives, must not be null.
     */
    public Precedence(final Alternatives alternatives) {
        super(alternatives);
    }

    @Override
    public boolean isRepetition() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean isPrecedence() {
        return true;
    }
}
