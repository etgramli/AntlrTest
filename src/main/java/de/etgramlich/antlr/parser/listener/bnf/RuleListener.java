package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.bnf.type.Rule;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.AbstractId;
import de.etgramlich.antlr.util.SymbolTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static de.etgramlich.antlr.util.SymbolTable.SymbolType.LHS;


public class RuleListener extends bnfBaseListener {
    private Rule rule;

    @Override
    public void enterRule_(@NotNull bnfParser.Rule_Context ctx) {
        // LHS
        IdListener idListener = new IdListener();
        idListener.enterId(ctx.lhs().id());
        AbstractId lhs = idListener.getId();

        if (SymbolTable.contains(lhs.getText())) {
            throw new IllegalArgumentException("Duplicate rule: " + lhs.getText());
        }
        SymbolTable.add(lhs.getText(), LHS);

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
