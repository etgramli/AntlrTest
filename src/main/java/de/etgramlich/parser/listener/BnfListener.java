package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.BnfRule;
import de.etgramlich.parser.type.Bnf;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses an entire BNF (file) / multiple rules and returns a Bnf object.
 * Call sequence:
 * 1. new BnfListener
 * 2. enterBnf()
 * 3. getBnf()
 */
public final class BnfListener extends BnfBaseListener {
    /**
     * Contains the bnf with all rules.
     */
    private Bnf bnf;

    /**
     * Returns the parsed BNF. Must be called after enterBNF().
     * @return New bnf object after call to enterBnf().
     */
    public Bnf getBnf() {
        return bnf;
    }

    @Override
    public void enterBnf(final BnfParser.BnfContext ctx) {
        final List<BnfRule> bnfRules = new ArrayList<>(ctx.bnfrule().size());
        RuleListener listener = new RuleListener();

        for (BnfParser.BnfruleContext context : ctx.bnfrule()) {
            listener.enterBnfrule(context);
            bnfRules.add(listener.getBnfRule());
        }
        this.bnf = new Bnf(bnfRules);
    }

    @Override
    public void exitBnf(final BnfParser.BnfContext ctx) {
        super.exitBnf(ctx);
    }
}
