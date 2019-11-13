package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.type.Rule;
import de.etgramlich.antlr.parser.type.RuleList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class RuleListListener extends bnfBaseListener {
    private RuleList ruleList;

    @Override
    public void enterRulelist(@NotNull bnfParser.RulelistContext ctx) {
        List<Rule> rules = new ArrayList<>(ctx.rule_().size());
        RuleListener listener = new RuleListener();

        for (bnfParser.Rule_Context context : ctx.rule_()) {
            listener.enterRule_(context);
            rules.add(listener.getRule());
        }
        this.ruleList = new RuleList(rules);
    }

    @Override
    public void exitRulelist(bnfParser.RulelistContext ctx) {
        super.exitRulelist(ctx);
    }

    @Contract(pure = true)
    public RuleList getRuleList() {
        return ruleList;
    }
}
