package de.etgramlich.antlr.semanticmodel.scope;

import org.jetbrains.annotations.Contract;

public final class RepetitionScope implements Scope {
    private final String name;
    private Scope next;

    @Contract(pure = true)
    public RepetitionScope(final String name) {
        this.name = name;
    }

    @Contract(pure = true)
    @Override
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    @Override
    public Scope accept(String terminal) {
        // ToDo
        return next;
    }
}
