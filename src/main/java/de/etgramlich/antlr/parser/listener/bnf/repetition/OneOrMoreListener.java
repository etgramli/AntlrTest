package de.etgramlich.antlr.parser.listener.bnf.repetition;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.AlternativesListener;
import de.etgramlich.antlr.parser.listener.bnf.type.Alternatives;
import org.jetbrains.annotations.NotNull;

public class OneOrMoreListener extends bnfBaseListener {
    private Alternatives alternatives;

    @Override
    public void enterOneormore(@NotNull bnfParser.OneormoreContext ctx) {
        AlternativesListener listener = new AlternativesListener();
        listener.enterAlternatives(ctx.alternatives());
        alternatives = listener.getAlternatives();
    }

    @Override
    public void exitOneormore(bnfParser.OneormoreContext ctx) {
        super.exitOneormore(ctx);
    }

    public Alternatives getAlternatives() {
        return alternatives;
    }
}
