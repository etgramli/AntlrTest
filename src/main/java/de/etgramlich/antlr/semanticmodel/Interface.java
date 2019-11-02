package de.etgramlich.antlr.semanticmodel;

import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Interface {
    private final String name;
    private final Set<Method> methods;

    public Interface(final String name) {
        this(name, Collections.emptyList());
    }

    public Interface(final String name, final Collection<Method> methods) {
        this.name = name;
        this.methods = new HashSet<>(methods);
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    public Set<Method> getMethods() {
        return methods;
    }
}
