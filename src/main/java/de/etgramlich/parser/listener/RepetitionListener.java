package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.repetition.AbstractRepetition;
import de.etgramlich.parser.type.repetition.Optional;
import de.etgramlich.parser.type.repetition.Precedence;
import de.etgramlich.parser.type.repetition.ZeroOrMore;
import org.jetbrains.annotations.NotNull;

public final class RepetitionListener extends BnfBaseListener {
    private AbstractRepetition repetition;

    public AbstractRepetition getRepetition() {
        return repetition;
    }

    @Override
    public void enterZeroormore(@NotNull BnfParser.ZeroormoreContext ctx) {
        AlternativeListener listener = new AlternativeListener();
        listener.enterAlternatives(ctx.alternatives());
        repetition = new ZeroOrMore(listener.getAlternatives());
    }

    @Override
    public void exitZeroormore(BnfParser.ZeroormoreContext ctx) {
        super.exitZeroormore(ctx);
    }

    @Override
    public void enterOptional(@NotNull BnfParser.OptionalContext ctx) {
        AlternativeListener listener = new AlternativeListener();
        listener.enterAlternatives(ctx.alternatives());
        repetition = new Optional(listener.getAlternatives());
    }

    @Override
    public void exitOptional(BnfParser.OptionalContext ctx) {
        super.exitOptional(ctx);
    }

    @Override
    public void enterPrecedence(BnfParser.PrecedenceContext ctx) {
        AlternativeListener listener = new AlternativeListener();
        listener.enterAlternatives(ctx.alternatives());
        repetition = new Precedence(listener.getAlternatives());
    }
}
