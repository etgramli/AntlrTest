package de.etgramlich.parser.type.repetition;

import de.etgramlich.parser.type.Alternatives;

public final class ZeroOrMore extends AbstractRepetition {
    public ZeroOrMore(Alternatives alternatives) {
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
