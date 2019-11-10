package de.etgramlich.antlr.semanticmodel.scope;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class AlternativeScope implements Scope {
    private final String name;
    private final List<String> terminals;
    private Scope next;

    public AlternativeScope(final String name, final Scope next) {
        this(name, next, Collections.emptyList());
    }

    public AlternativeScope(final String name, final Scope next, final Collection<String> terminals) {
        this.name = name;
        this.next = next;
        this.terminals = new ArrayList<>(terminals);
    }

    @Contract(pure = true)
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Scope accept(String terminal) {
        // ToDo
        return next;
    }
}
