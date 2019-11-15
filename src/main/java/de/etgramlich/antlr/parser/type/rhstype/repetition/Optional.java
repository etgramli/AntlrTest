package de.etgramlich.antlr.parser.type.rhstype.repetition;

import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class Optional extends AbstractRepetition {
    public Optional(Collection<Alternative> alternatives) {
        super(alternatives);
        if (alternatives.size() > 1) {
            throw new IllegalArgumentException("Optional argument has more than one Alternatives! (must be 0 or 1)");
        }
    }

    @Override
    public boolean isRepetition() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public boolean isPrecedence() {
        return false;
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        getAlternatives().forEach(alternative -> alternative.accept(visitor));
        visitor.visit(this);
    }
}
