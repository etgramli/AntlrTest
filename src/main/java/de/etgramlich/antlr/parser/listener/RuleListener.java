package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.type.Rule;
import de.etgramlich.antlr.parser.listener.type.terminal.AbstractId;
import de.etgramlich.antlr.util.SymbolTable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RuleListener extends bnfBaseListener {
    private Rule rule;

    @Override
    public void enterRule_(@NotNull bnfParser.Rule_Context ctx) {
        // LHS
        IdListener idListener = new IdListener();
        idListener.enterId(ctx.lhs().id());
        AbstractId lhs = idListener.getId();

        // RHS
        AlternativeListener alternativesListener = new AlternativeListener();
        alternativesListener.enterAlternatives(ctx.rhs().alternatives());
        List<Alternative> rhs = alternativesListener.getAlternatives();

        rule = new Rule(lhs, rhs);

        if (!SymbolTable.add(lhs.getText(), rule)) {
            throw new IllegalArgumentException("Duplicate rule: " + lhs.getText());
        }
    }

    @Override
    public void exitRule_(bnfParser.Rule_Context ctx) {
        super.exitRule_(ctx);
    }

    @Contract(pure = true)
    public Rule getRule() {
        return rule;
    }
}
