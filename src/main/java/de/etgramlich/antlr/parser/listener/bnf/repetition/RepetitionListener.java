package de.etgramlich.antlr.parser.listener.bnf.repetition;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.AlternativesListener;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Alternative;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class RepetitionListener extends bnfBaseListener {
    protected List<Alternative> alternatives;

    @NotNull
    @Contract(pure = true)
    public List<Alternative> getAlternatives() {
        return Collections.unmodifiableList(alternatives);
    }

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

    @Override
    public void enterOptional(@NotNull bnfParser.OptionalContext ctx) {
        if (ctx.alternatives().alternative().size() > 1) {
            throw new IllegalArgumentException("Optional must only have 0 or 1 element! (had: " +
                    ctx.alternatives().alternative().size() + ")");
        }
        AlternativesListener listener = new AlternativesListener();
        listener.enterAlternatives(ctx.alternatives());
        this.alternatives = listener.getAlternatives();
    }

    @Override
    public void exitOptional(bnfParser.OptionalContext ctx) {
        super.exitOptional(ctx);
    }
}
