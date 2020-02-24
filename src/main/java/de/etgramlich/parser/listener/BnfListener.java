package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.BnfRule;
import de.etgramlich.parser.type.Bnf;

import java.util.ArrayList;
import java.util.List;

public final class BnfListener extends BnfBaseListener {
    private Bnf bnf;

    @Override
    public void enterBnf(BnfParser.BnfContext ctx) {
        final List<BnfRule> bnfRules = new ArrayList<>(ctx.bnfrule().size());
        RuleListener listener = new RuleListener();

        for (BnfParser.BnfruleContext context : ctx.bnfrule()) {
            listener.enterBnfrule(context);
            bnfRules.add(listener.getBnfRule());
        }
        this.bnf = new Bnf(bnfRules);
    }

    @Override
    public void exitBnf(BnfParser.BnfContext ctx) {
        super.exitBnf(ctx);
    }

    public Bnf getBnf() {
        return bnf;
    }
}
