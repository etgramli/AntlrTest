package de.etgramlich.antlr.parser.listener.type.terminal;

import org.jetbrains.annotations.Contract;

public final class Id extends AbstractId {
    public Id(String id) {
        super(id);
    }
    @Contract(pure = true)
    @Override
    public boolean isTerminal() {
        return false;
    }
}
