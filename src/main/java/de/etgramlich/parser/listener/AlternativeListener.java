package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.Element;
import de.etgramlich.parser.type.Sequence;

import java.util.ArrayList;
import java.util.List;

public final class AlternativeListener extends BnfBaseListener {
    /**
     * Current (ebnf) sequence being processed.
     */
    private Sequence sequence;

    /**
     * List of Sequence of that the resulting Alternatives is composed.
     */
    private final List<Sequence> alternatives = new ArrayList<>();

    /**
     * Returns a newly created Alternatives of parsed sequences. Must be called after enterAlternatives()!
     * @return New Alternatives object.
     */
    public Alternatives getAlternatives() {
        return new Alternatives(alternatives);
    }

    @Override
    public void enterAlternatives(final BnfParser.AlternativesContext ctx) {
        for (BnfParser.SequenceContext context : ctx.sequence()) {
            enterSequence(context);
            alternatives.add(sequence);
        }
    }

    @Override
    public void enterSequence(final BnfParser.SequenceContext ctx) {
        List<Element> elements = new ArrayList<>(ctx.getChildCount());
        ElementListener listener = new ElementListener();

        for (BnfParser.ElementContext elementContext : ctx.element()) {
            listener.enterElement(elementContext);
            elements.add(listener.getElement());
        }
        sequence = new Sequence(elements);
    }

    @Override
    public void exitSequence(final BnfParser.SequenceContext ctx) {
        super.exitSequence(ctx);
    }

    @Override
    public void exitAlternatives(final BnfParser.AlternativesContext ctx) {
        super.exitAlternatives(ctx);
    }
}
