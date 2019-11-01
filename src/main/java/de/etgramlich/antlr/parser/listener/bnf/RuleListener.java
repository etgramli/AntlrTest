package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.bnf.type.Rule;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.ID;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class RuleListener extends bnfBaseListener {
    private Rule rule;

    @Override
    public void enterRule_(@NotNull bnfParser.Rule_Context ctx) {
        // LHS
        IdListener idListener = new IdListener();
        idListener.enterId(ctx.lhs().id());
        ID lhs = idListener.getId();

        // RHS
        AlternativesListener alternativesListener = new AlternativesListener();
        alternativesListener.enterAlternatives(ctx.rhs().alternatives());
        List<Alternative> rhs = alternativesListener.getAlternatives();

        rule = new Rule(lhs, rhs);
    }

    @Override
    public void exitRule_(bnfParser.Rule_Context ctx) {
        super.exitRule_(ctx);
    }

    public Rule getRule() {
        return rule;
    }
}
