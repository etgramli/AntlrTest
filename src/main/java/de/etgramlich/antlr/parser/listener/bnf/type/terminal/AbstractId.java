package de.etgramlich.antlr.parser.listener.bnf.type.terminal;

import org.jetbrains.annotations.Contract;

public abstract class AbstractId implements ID {
    private final String id;

    @Contract(pure = true)
    AbstractId(final String id) {
        this.id = id;
    }

    @Override
    public String getText() {
        return id;
    }
}
