package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.type.rhstype.repetition.AbstractRepetition;
import de.etgramlich.antlr.parser.type.rhstype.repetition.Optional;
import de.etgramlich.antlr.parser.type.rhstype.repetition.Precedence;
import de.etgramlich.antlr.parser.type.rhstype.repetition.ZeroOrMore;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class RepetitionListener extends bnfBaseListener {
    private AbstractRepetition repetition;

    @Contract(pure = true)
    public AbstractRepetition getRepetition() {
        return repetition;
    }

    @Override
    public void enterZeroormore(@NotNull bnfParser.ZeroormoreContext ctx) {
        AlternativeListener listener = new AlternativeListener();
        listener.enterAlternatives(ctx.alternatives());
        repetition = new ZeroOrMore(listener.getAlternatives());
    }

    @Override
    public void exitZeroormore(bnfParser.ZeroormoreContext ctx) {
        super.exitZeroormore(ctx);
    }

    @Override
    public void enterOptional(@NotNull bnfParser.OptionalContext ctx) {
        AlternativeListener listener = new AlternativeListener();
        listener.enterAlternatives(ctx.alternatives());
        repetition = new Optional(listener.getAlternatives());
    }

    @Override
    public void exitOptional(bnfParser.OptionalContext ctx) {
        super.exitOptional(ctx);
    }

    @Override
    public void enterPrecedence(bnfParser.PrecedenceContext ctx) {
        AlternativeListener listener = new AlternativeListener();
        listener.enterAlternatives(ctx.alternatives());
        repetition = new Precedence(listener.getAlternatives());
    }
}
