package de.etgramlich.antlr.semanticmodel.scope;

import org.jetbrains.annotations.Contract;

public final class SequenceScope implements Scope {
    private final String name;
    private final Scope next;

    @Contract(pure = true)
    public SequenceScope(final String name, final Scope next) {
        this.name = name;
        this.next = next;
    }

    @Contract(pure = true)
    @Override
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    @Override
    public Scope accept(String terminal) {
        return next;
    }
}
