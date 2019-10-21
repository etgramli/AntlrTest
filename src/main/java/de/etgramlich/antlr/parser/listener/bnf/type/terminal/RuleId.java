package de.etgramlich.antlr.parser.listener.bnf.type.terminal;

import org.jetbrains.annotations.Contract;

public final class RuleId extends AbstractId {
    @Contract(pure = true)
    public RuleId(final String id) {
        super(id);
    }
}
