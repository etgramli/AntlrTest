package de.etgramlich.antlr.parser.listener.type.terminal;

import org.jetbrains.annotations.Contract;

public final class RuleId extends AbstractId {
    @Contract(pure = true)
    public RuleId(final String id) {
        super(id);
    }
    @Contract(pure = true)
    @Override
    public boolean isTerminal() {
        return false;
    }
}
