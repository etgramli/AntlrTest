package de.etgramlich.antlr.parser.listener.bnf.type;

import de.etgramlich.antlr.util.BnfElement;
import de.etgramlich.antlr.util.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public final class Alternatives implements BnfElement {
    private List<Alternative> alternatives;

    public Alternatives(final Collection<Alternative> alternatives) {
        this.alternatives = List.copyOf(alternatives);
    }

    @NotNull
    @Contract(pure = true)
    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        alternatives.forEach(alternative -> alternative.accept(visitor));
        visitor.visit(this);
    }
}
