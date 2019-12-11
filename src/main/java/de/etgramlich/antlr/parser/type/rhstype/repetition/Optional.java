package de.etgramlich.antlr.parser.type.rhstype.repetition;

import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import org.jetbrains.annotations.Contract;

import java.util.Collection;

public final class Optional extends AbstractRepetition {
    public Optional(Collection<Alternative> alternatives) {
        super(alternatives);
        if (alternatives.size() > 1) {
            throw new IllegalArgumentException("Optional argument has more than one Alternatives! (must be 0 or 1)");
        }
    }

    @Contract(pure = true)
    @Override
    public boolean isRepetition() {
        return false;
    }

    @Contract(pure = true)
    @Override
    public boolean isOptional() {
        return true;
    }

    @Contract(pure = true)
    @Override
    public boolean isPrecedence() {
        return false;
    }
}
