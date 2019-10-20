package de.etgramlich.antlr.parser.listener.bnf.type.terminal;

import org.jetbrains.annotations.Contract;

public class RuleId implements ID {

    private final String id;

    @Contract(pure = true)
    public RuleId(String id) {
        this.id = id;
    }

    @Override
    public String getText() {
        return id;
    }
}
