package de.etgramlich.parser.type.rhstype.repetition;

import de.etgramlich.parser.type.rhstype.Alternative;
import org.jetbrains.annotations.Contract;

import java.util.Collection;

public final class Precedence extends AbstractRepetition {
    public Precedence(Collection<Alternative> alternatives) {
        super(alternatives);
    }

    @Contract(pure = true)
    @Override
    public boolean isRepetition() {
        return false;
    }

    @Contract(pure = true)
    @Override
    public boolean isOptional() {
        return false;
    }

    @Contract(pure = true)
    @Override
    public boolean isPrecedence() {
        return true;
    }
}
