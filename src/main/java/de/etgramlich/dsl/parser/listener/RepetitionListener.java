package de.etgramlich.dsl.parser.listener;

import de.etgramlich.dsl.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.dsl.parser.gen.bnf.BnfParser;
import de.etgramlich.dsl.parser.type.repetition.AbstractRepetition;
import de.etgramlich.dsl.parser.type.repetition.Optional;
import de.etgramlich.dsl.parser.type.repetition.Precedence;
import de.etgramlich.dsl.parser.type.repetition.ZeroOrMore;

/**
 * Parses a Repetition, optional element or a precedence of EBNF.
 * Call sequence:
 * 1. new RepetitionListener()
 * 2. enterZeroOrMore() or enterOptional() or enterPrecedence()
 * 3. getRepetition()
 */
public final class RepetitionListener extends BnfBaseListener {
    /**
     * Bnf repetition element.
     */
    private AbstractRepetition repetition;

    /**
     * Queries the repetition element. Must be called after enterZeroOrMore(), enterOptional() or enterPrecedence()!
     * @return A subtype object of AbstractRepetition after call, else null.
     */
    public AbstractRepetition getRepetition() {
        return repetition;
    }

    @Override
    public void enterZeroormore(final BnfParser.ZeroormoreContext ctx) {
        AlternativeListener listener = new AlternativeListener();
        listener.enterAlternatives(ctx.alternatives());
        repetition = new ZeroOrMore(listener.getAlternatives());
    }

    @Override
    public void enterOptional(final BnfParser.OptionalContext ctx) {
        AlternativeListener listener = new AlternativeListener();
        listener.enterAlternatives(ctx.alternatives());
        repetition = new Optional(listener.getAlternatives());
    }

    @Override
    public void enterPrecedence(final BnfParser.PrecedenceContext ctx) {
        AlternativeListener listener = new AlternativeListener();
        listener.enterAlternatives(ctx.alternatives());
        repetition = new Precedence(listener.getAlternatives());
    }
}
