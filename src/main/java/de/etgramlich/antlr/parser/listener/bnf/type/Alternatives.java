package de.etgramlich.antlr.parser.listener.bnf.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Alternatives {
    private List<Alternative> alternatives;

    public Alternatives(final Collection<Alternative> alternatives) {
        this.alternatives = new ArrayList<>(alternatives);
    }

    @NotNull
    @Contract(pure = true)
    public List<Alternative> getAlternatives() {
        return Collections.unmodifiableList(alternatives);
    }
}
