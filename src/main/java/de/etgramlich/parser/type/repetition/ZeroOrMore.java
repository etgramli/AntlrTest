package de.etgramlich.parser.type.repetition;

import de.etgramlich.parser.type.Alternatives;
import org.jetbrains.annotations.Contract;

public final class ZeroOrMore extends AbstractRepetition {
    public ZeroOrMore(Alternatives alternatives) {
        super(alternatives);
    }

    @Contract(pure = true)
    @Override
    public boolean isRepetition() {
        return true;
    }

    @Contract(pure = true)
    @Override
    public boolean isOptional() {
        return false;
    }

    @Contract(pure = true)
    @Override
    public boolean isPrecedence() {
        return false;
    }
}
