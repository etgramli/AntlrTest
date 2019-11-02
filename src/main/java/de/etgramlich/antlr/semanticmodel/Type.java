package de.etgramlich.antlr.semanticmodel;

import org.jetbrains.annotations.Contract;

public final class Type {
    private final String name;

    @Contract(pure = true)
    public Type(final String name) {
        this.name = name;
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }
}
