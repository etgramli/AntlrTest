package de.etgramlich.antlr.util.graph;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Scope {
    private final String name;

    @Contract(pure = true)
    public Scope(@NotNull final String name) {
        this.name = name;
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scope scope = (Scope) o;

        return name.equals(scope.name);
    }

    @Contract(pure = true)
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
