package de.etgramlich.antlr.semanticmodel.type;

import org.jetbrains.annotations.Contract;

public final class Type {
    private String name;

    @Contract(pure = true)
    public Type() {}

    @Contract(pure = true)
    public Type(final String name) {
        this.name = name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }
}
