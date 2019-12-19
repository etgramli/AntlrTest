package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.terminal.AbstractId;
import de.etgramlich.parser.type.terminal.Id;
import de.etgramlich.parser.type.terminal.RuleId;
import de.etgramlich.parser.type.terminal.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Listener that queries the IDs (ruleid and id) and text rules of the ANTLR4 BNF grammar.
 * The rule function must be called according to the current rule. The (unescaped id) string can be queried with getText().
 */
public final class IdListener extends BnfBaseListener {
    private AbstractId id;

    @Override
    public void enterKeyword(@NotNull BnfParser.KeywordContext ctx) {
        String textString = ctx.getText().trim();
        id = new Text(textString);
    }

    @Override
    public void exitKeyword(BnfParser.KeywordContext ctx) {
        super.exitKeyword(ctx);
    }

    @Override
    public void enterType(@NotNull BnfParser.TypeContext ctx) {
        String trimmedRuleId = ctx.getText().trim();
        id = new RuleId(trimmedRuleId);
    }

    @Override
    public void exitType(BnfParser.TypeContext ctx) {
        super.exitType(ctx);
    }

    @Override
    public void enterNt(@NotNull BnfParser.NtContext ctx) {
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
    public void exitNt(BnfParser.NtContext ctx) {
        super.exitNt(ctx);
    }


    @Contract(pure = true)
    public AbstractId getId() {
        return id;
    }
}
