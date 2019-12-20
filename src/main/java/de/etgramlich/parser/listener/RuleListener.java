package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.BnfRule;
import de.etgramlich.parser.type.NonTerminal;
import de.etgramlich.util.SymbolTable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class RuleListener extends BnfBaseListener {
    private BnfRule bnfRule;

    @Override
    public void enterBnfrule(@NotNull BnfParser.BnfruleContext ctx) {
        // LHS
        NonTerminalListener idListener = new NonTerminalListener();
        idListener.enterNt(ctx.lhs().nt());
        NonTerminal lhs = idListener.getNonTerminal();

        // RHS
        AlternativeListener alternativesListener = new AlternativeListener();
        alternativesListener.enterAlternatives(ctx.rhs().alternatives());
        Alternatives rhs = alternativesListener.getAlternatives();

        bnfRule = new BnfRule(lhs, rhs);

        if (!SymbolTable.add(lhs.getName(), bnfRule)) {
            throw new IllegalArgumentException("Duplicate rule: " + lhs.getName());
        }
    }

    @Override
    public void exitBnfrule(BnfParser.BnfruleContext ctx) {
        super.exitBnfrule(ctx);
    }

    @Contract(pure = true)
    public BnfRule getBnfRule() {
        return bnfRule;
    }
}
