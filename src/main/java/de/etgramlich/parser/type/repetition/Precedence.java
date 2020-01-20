package de.etgramlich.parser.type.repetition;

import de.etgramlich.parser.type.Alternatives;

public final class Precedence extends AbstractRepetition {
    public Precedence(Alternatives alternatives) {
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
