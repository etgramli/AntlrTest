package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.AbstractId;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.Id;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.RuleId;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Listener that queries the IDs (ruleid and id) and text rules of the ANTLR4 BNF grammar.
 * The rule function must be called according to the current rule. The (unescaped id) string can be queried with getText().
 */
public final class IdListener extends bnfBaseListener {
    private AbstractId id;

    @Override
    public void enterText(@NotNull bnfParser.TextContext ctx) {
        String textString = ctx.getText().trim();
        id = new Text(textString);
    }

    @Override
    public void exitText(bnfParser.TextContext ctx) {
        super.exitText(ctx);
    }

    @Override
    public void enterRuleid(@NotNull bnfParser.RuleidContext ctx) {
        String trimmedRuleId = ctx.getText().trim();
        id = new RuleId(trimmedRuleId);
    }

    @Override
    public void exitRuleid(bnfParser.RuleidContext ctx) {
        super.exitRuleid(ctx);
    }

    @Override
    public void enterId(@NotNull bnfParser.IdContext ctx) {
        String strippedId = stripLTGT(ctx.getText());
        id = new Id(strippedId);
    }

    @NotNull
    public static String stripLTGT(@NotNull final String string) {
        final String trimmed = string.trim();
        final String noLTGT = trimmed.substring(1, trimmed.length()-1);
        return noLTGT.trim();
    }

    @Override
    public void exitId(bnfParser.IdContext ctx) {
        super.exitId(ctx);
    }


    @Contract(pure = true)
    public AbstractId getId() {
        return id;
    }
}
