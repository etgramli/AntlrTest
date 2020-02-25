package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.text.Keyword;
import de.etgramlich.parser.type.text.NonTerminal;
import de.etgramlich.parser.type.text.Type;

public final class NonTerminalListener extends BnfBaseListener {
    // ToDo: Merge variables
    /**
     * Non-terminal bnf element.
     */
    private NonTerminal id;

    /**
     * Keyword bnf element.
     */
    private Keyword keyword;

    /**
     * Type bnf element.
     */
    private Type type;

    /**
     * Returns non-terminal bnf element. Must be called after enterNt()!
     * @return Non-terminal object after call, else null.
     */
    public NonTerminal getNonTerminal() {
        return id;
    }

    /**
     * Returns keyword bnf element. Must be called after enterKeyword()!
     * @return Keyword element, after call to enterKeyword(), else null.
     */
    public Keyword getKeyword() {
        return keyword;
    }

    /**
     * Returns type bnf element. Must be called after enterType()!
     * @return Type object after call to enterType(), else null.
     */
    public Type getType() {
        return type;
    }

    @Override
    public void enterKeyword(final BnfParser.KeywordContext ctx) {
        keyword = new Keyword(ctx.getText().trim());
    }

    @Override
    public void exitKeyword(final BnfParser.KeywordContext ctx) {
        super.exitKeyword(ctx);
    }

    @Override
    public void enterType(final BnfParser.TypeContext ctx) {
        type = new Type(ctx.getText().trim());
    }

    @Override
    public void exitType(final BnfParser.TypeContext ctx) {
        super.exitType(ctx);
    }

    @Override
    public void enterNt(final BnfParser.NtContext ctx) {
        id = new NonTerminal(stripLTGT(ctx.getText()));
    }

    @Override
    public void exitNt(final BnfParser.NtContext ctx) {
        super.exitNt(ctx);
    }

    /**
     * Strips the surrounding braces.
     * @param string String, must not be blank.
     * @return String without the left- and rightmost characters.
     */
    public static String stripLTGT(final String string) {
        final String trimmed = string.trim();
        return trimmed.substring(1, trimmed.length() - 1).trim();
    }
}
