package de.etgramlich.antlr.parser.listener.bnf.type.terminal;

import de.etgramlich.antlr.util.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
