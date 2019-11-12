package de.etgramlich.antlr.parser.listener.type.rhstype.repetition;

import de.etgramlich.antlr.parser.listener.type.rhstype.Alternative;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class OneOrMore extends AbstractRepetition {
    public OneOrMore(Collection<Alternative> alternatives) {
        super(alternatives);
        if (alternatives.size() == 0) {
            throw new IllegalArgumentException("OneOrMore must have at least 1 Alternative!");
        }
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        getAlternatives().forEach(alternative -> alternative.accept(visitor));
        visitor.visit(this);
    }
}
