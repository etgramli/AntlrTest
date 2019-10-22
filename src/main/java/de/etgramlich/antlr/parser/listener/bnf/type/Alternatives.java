package de.etgramlich.antlr.parser.listener.bnf.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public final class Alternatives implements BnfType {
    private List<Alternative> alternatives;

    public Alternatives(final Collection<Alternative> alternatives) {
        this.alternatives = List.copyOf(alternatives);
    }

    @NotNull
    @Contract(pure = true)
    public List<Alternative> getAlternatives() {
        return alternatives;
    }

}
