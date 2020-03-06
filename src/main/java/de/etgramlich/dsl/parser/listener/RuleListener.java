package de.etgramlich.dsl.parser.listener;

import de.etgramlich.dsl.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.dsl.parser.gen.bnf.BnfParser;
import de.etgramlich.dsl.parser.type.Alternatives;
import de.etgramlich.dsl.parser.type.BnfRule;
import de.etgramlich.dsl.parser.type.text.NonTerminal;
import de.etgramlich.dsl.parser.type.text.TextElement;

/**
 * Parses a rule of EBNF.
 * Call sequence:
 * 1. new RuleListener()
 * 2. enterBnfRule()
 * 3. getBnfRule()
 */
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
        TextElement textElement = idListener.getTextElement();
        if (!(textElement instanceof NonTerminal)) {
            throw new IllegalArgumentException("Text element is not a NonTerminal on LHS!");
        }
        final NonTerminal lhs = (NonTerminal) textElement;

        AlternativeListener alternativesListener = new AlternativeListener();
        alternativesListener.enterAlternatives(ctx.rhs().alternatives());
        final Alternatives rhs = alternativesListener.getAlternatives();

        bnfRule = new BnfRule(lhs, rhs);
    }

    @Override
    public void exitBnfrule(final BnfParser.BnfruleContext ctx) {
        super.exitBnfrule(ctx);
    }
}
