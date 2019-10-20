package de.etgramlich.antlr.parser.listener.bnf.type;

import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.RuleId;
import org.jetbrains.annotations.Contract;

public final class Rule {
    private ID lhs;
    private Alternatives rhs;

    @Contract(pure = true)
    public Rule(final ID lhs, final Alternatives rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }


    @Contract(pure = true)
    public ID getLhs() {
        return lhs;
    }

    @Contract(pure = true)
    public Alternatives getRhs() {
        return rhs;
    }
}
