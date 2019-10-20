package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Listener that queries the IDs (ruleid and id) and text rules of the ANTLR4 BNF grammar.
 * The rule function must be called according to the current rule. The (unescaped id) string can be queried with getText().
 */
public final class IdListener extends bnfBaseListener {
    private String text;

    @Override
    public void enterText(@NotNull bnfParser.TextContext ctx) {
        this.text = ctx.getText().trim();
    }

    @Override
    public void exitText(bnfParser.TextContext ctx) {
        super.exitText(ctx);
    }

    @Override
    public void enterRuleid(@NotNull bnfParser.RuleidContext ctx) {
        this.text = ctx.getText().trim();
    }

    @Override
    public void exitRuleid(bnfParser.RuleidContext ctx) {
        super.exitRuleid(ctx);
    }

    @Override
    public void enterId(@NotNull bnfParser.IdContext ctx) {
        this.text = stripLTGT(ctx.getText());
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
    public String getText() {
        return text;
    }
}
