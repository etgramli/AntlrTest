package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;

public class OptionalListener extends bnfBaseListener {
    @Override
    public void enterOptional(bnfParser.OptionalContext ctx) {
        // ToDo: Strip [] and parse alternatives
        super.enterOptional(ctx);
    }

    @Override
    public void exitOptional(bnfParser.OptionalContext ctx) {
        super.exitOptional(ctx);
    }
}
