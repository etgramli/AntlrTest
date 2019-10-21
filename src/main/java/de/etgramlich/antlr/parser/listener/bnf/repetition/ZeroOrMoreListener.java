package de.etgramlich.antlr.parser.listener.bnf.repetition;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.AlternativesListener;
import de.etgramlich.antlr.parser.listener.bnf.type.Alternatives;
import org.jetbrains.annotations.NotNull;

public class ZeroOrMoreListener extends bnfBaseListener {
    private Alternatives alternatives;

    @Override
    public void enterZeroormore(@NotNull bnfParser.ZeroormoreContext ctx) {
        AlternativesListener listener = new AlternativesListener();
        listener.enterAlternatives(ctx.alternatives());
        this.alternatives = listener.getAlternatives();
    }

    @Override
    public void exitZeroormore(bnfParser.ZeroormoreContext ctx) {
        super.exitZeroormore(ctx);
    }

    public Alternatives getAlternatives() {
        return alternatives;
    }
}
