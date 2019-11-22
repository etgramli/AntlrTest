package de.etgramlich.antlr.parser.type.rhstype.repetition;

import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class ZeroOrMore extends AbstractRepetition {
    public ZeroOrMore(Collection<Alternative> alternatives) {
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

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        getAlternatives().forEach(alternative -> alternative.accept(visitor));
        visitor.visit(this);
    }

    @Override
    public void removeNonTerminals() {
        // ToDo
    }
}
