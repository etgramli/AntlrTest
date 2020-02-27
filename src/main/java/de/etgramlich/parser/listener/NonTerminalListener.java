package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.text.Keyword;
import de.etgramlich.parser.type.text.NonTerminal;
import de.etgramlich.parser.type.text.TextElement;
import de.etgramlich.parser.type.text.Type;

/**
 * Parses a text element of BNF.
 * Call sequence:
 * 1. new NonTerminalListener()
 * 2. enterKeyword() or enterType() or enterNt()
 * 3. getTextElement()
 */
public final class NonTerminalListener extends BnfBaseListener {
    /**
     * TextElement bnf element.
     */
    private TextElement textElement;

    /**
     * Returns non-terminal bnf element. Must be called after enterNt(), enterKeyword() or enterType()!
     * @return Non-terminal object after call, else null.
     */
    public TextElement getTextElement() {
        return textElement;
    }

    @Override
    public void enterKeyword(final BnfParser.KeywordContext ctx) {
        textElement = new Keyword(ctx.getText().trim());
    }

    @Override
    public void exitKeyword(final BnfParser.KeywordContext ctx) {
        super.exitKeyword(ctx);
    }

    @Override
    public void enterType(final BnfParser.TypeContext ctx) {
        textElement = new Type(ctx.getText().trim());
    }

    @Override
    public void exitType(final BnfParser.TypeContext ctx) {
        super.exitType(ctx);
    }

    @Override
    public void enterNt(final BnfParser.NtContext ctx) {
        textElement = new NonTerminal(stripLTGT(ctx.getText()));
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
