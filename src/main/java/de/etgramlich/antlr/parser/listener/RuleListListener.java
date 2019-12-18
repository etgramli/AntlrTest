package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.BnfParser;
import de.etgramlich.antlr.parser.type.Rule;
import de.etgramlich.antlr.parser.type.RuleList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class RuleListListener extends BnfBaseListener {
    private RuleList ruleList;

    @Override
    public void enterBnf(@NotNull BnfParser.BnfContext ctx) {
        List<Rule> rules = new ArrayList<>(ctx.bnfrule().size());
        RuleListener listener = new RuleListener();

        for (BnfParser.BnfruleContext context : ctx.bnfrule()) {
            listener.enterBnfrule(context);
            rules.add(listener.getRule());
        }
        this.ruleList = new RuleList(rules);
    }

    @Override
    public void exitBnf(BnfParser.BnfContext ctx) {
        super.exitBnf(ctx);
    }

    @Contract(pure = true)
    public RuleList getRuleList() {
        return ruleList;
    }
}
