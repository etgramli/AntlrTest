package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.type.Alternatives;
import de.etgramlich.antlr.parser.listener.bnf.type.Rule;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.Id;
import org.jetbrains.annotations.NotNull;


public class RuleListener extends bnfBaseListener {
    private Rule rule;

    @Override
    public void enterRule_(@NotNull bnfParser.Rule_Context ctx) {
        // LHS
        IdListener idListener = new IdListener();
        idListener.enterId(ctx.lhs().id());
        Id lhs = idListener.getId();

        // RHS
        AlternativesListener alternativesListener = new AlternativesListener();
        alternativesListener.enterAlternatives(ctx.rhs().alternatives());
        Alternatives rhs = alternativesListener.getAlternatives();

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
