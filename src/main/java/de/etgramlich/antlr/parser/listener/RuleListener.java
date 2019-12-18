package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.BnfParser;
import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.Rule;
import de.etgramlich.antlr.parser.type.terminal.AbstractId;
import de.etgramlich.antlr.util.SymbolTable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RuleListener extends BnfBaseListener {
    private Rule rule;

    @Override
    public void enterBnfrule(@NotNull BnfParser.BnfruleContext ctx) {
        // LHS
        IdListener idListener = new IdListener();
        idListener.enterNt(ctx.lhs().nt());
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
    public void exitBnfrule(BnfParser.BnfruleContext ctx) {
        super.exitBnfrule(ctx);
    }

    @Contract(pure = true)
    public Rule getRule() {
        return rule;
    }
}
