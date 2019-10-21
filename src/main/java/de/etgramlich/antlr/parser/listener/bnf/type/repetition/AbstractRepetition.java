package de.etgramlich.antlr.parser.listener.bnf.type.repetition;

import de.etgramlich.antlr.parser.listener.bnf.type.Alternative;
import de.etgramlich.antlr.util.BnfElement;
import de.etgramlich.antlr.util.BnfTypeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractRepetition implements BnfElement {
    private final List<Alternative> alternatives;

    AbstractRepetition(final Collection<Alternative> alternatives) {
        this.alternatives = List.copyOf(alternatives);
    }

    public List<Alternative> getAlternatives() {
        return Collections.unmodifiableList(alternatives);
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        alternatives.forEach(alternative -> alternative.accept(visitor));
        visitor.visit(this);
    }
}
