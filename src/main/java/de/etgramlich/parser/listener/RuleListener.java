package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.BnfRule;
import de.etgramlich.parser.type.text.NonTerminal;
import de.etgramlich.util.SymbolTable;

public final class RuleListener extends BnfBaseListener {
    /**
     * Bnf rule element.
     */
    private BnfRule bnfRule;

    /**
     * Returns the parse BNF rule element. Must be called after enterBnfRule()!
     * @return BnfRule object after calling enterBnfRule, else null.
     */
    public BnfRule getBnfRule() {
        return bnfRule;
    }

    @Override
    public void enterBnfrule(final BnfParser.BnfruleContext ctx) {
        NonTerminalListener idListener = new NonTerminalListener();
        idListener.enterNt(ctx.lhs().nt());
        NonTerminal lhs = idListener.getNonTerminal();

        AlternativeListener alternativesListener = new AlternativeListener();
        alternativesListener.enterAlternatives(ctx.rhs().alternatives());
        Alternatives rhs = alternativesListener.getAlternatives();

        bnfRule = new BnfRule(lhs, rhs);

        if (!SymbolTable.add(lhs.getName(), bnfRule)) {
            throw new IllegalArgumentException("Duplicate rule: " + lhs.getName());
        }
    }

    @Override
    public void exitBnfrule(final BnfParser.BnfruleContext ctx) {
        super.exitBnfrule(ctx);
    }
}
