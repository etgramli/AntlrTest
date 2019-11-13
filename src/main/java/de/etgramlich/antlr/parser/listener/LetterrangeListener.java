package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.type.rhstype.LetterRange;
import de.etgramlich.antlr.util.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class LetterrangeListener extends bnfBaseListener {
    private LetterRange letterRange;

    @Override
    public void enterLetterrange(@NotNull bnfParser.LetterrangeContext ctx) {
        final String range = StringUtil.removeAllWhiteSpaces(ctx.getText());
        final char first = range.charAt(1);
        final char last = range.charAt(6);
        final boolean letter = !StringUtil.isAllNumeric(first, last);

        letterRange = new LetterRange(letter, first, last);
    }
    @Override
    public void exitLetterrange(bnfParser.LetterrangeContext ctx) {
        super.exitLetterrange(ctx);
    }

    @Contract(pure = true)
    public LetterRange getLetterRange() {
        return letterRange;
    }
}
