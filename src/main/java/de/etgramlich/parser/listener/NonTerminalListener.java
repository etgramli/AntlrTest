package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.text.Keyword;
import de.etgramlich.parser.type.text.NonTerminal;
import de.etgramlich.parser.type.text.Type;

/**
 * Listener that queries the IDs (ruleid and id) and text rules of the ANTLR4 BNF grammar.
 * The rule function must be called according to the current rule. The (unescaped id) string can be queried with getText().
 */
public final class NonTerminalListener extends BnfBaseListener {
    private NonTerminal id;
    private Keyword keyword;
    private Type type;

    public NonTerminal getNonTerminal() {
        return id;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void enterKeyword(BnfParser.KeywordContext ctx) {
        keyword = new Keyword(ctx.getText().trim());
    }

    @Override
    public void exitKeyword(BnfParser.KeywordContext ctx) {
        super.exitKeyword(ctx);
    }

    @Override
    public void enterType(BnfParser.TypeContext ctx) {
        type = new Type(ctx.getText().trim());
    }

    @Override
    public void exitType(BnfParser.TypeContext ctx) {
        super.exitType(ctx);
    }

    @Override
    public void enterNt(BnfParser.NtContext ctx) {
        id = new NonTerminal(stripLTGT(ctx.getText()));
    }

    public static String stripLTGT(final String string) {
        final String trimmed = string.trim();
        final String noLTGT = trimmed.substring(1, trimmed.length()-1);
        return noLTGT.trim();
    }

    @Override
    public void exitNt(BnfParser.NtContext ctx) {
        super.exitNt(ctx);
    }
}
