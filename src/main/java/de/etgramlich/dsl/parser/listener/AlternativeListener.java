package de.etgramlich.dsl.parser.listener;

import de.etgramlich.dsl.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.dsl.parser.gen.bnf.BnfParser;
import de.etgramlich.dsl.parser.type.Alternatives;
import de.etgramlich.dsl.parser.type.Element;
import de.etgramlich.dsl.parser.type.Sequence;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses a list of sequences of EBNF to one alternatives object.
 * The call sequence is:
 * 1. new AlternativeListener()
 * 2. enterAlternatives()
 * 3. getAlternatives()
 */
public final class AlternativeListener extends BnfBaseListener {
    /**
     * Current (ebnf) sequence being processed.
     */
    private Sequence sequence;

    /**
     * List of Sequence of that the resulting Alternatives is composed.
     */
    private List<Sequence> alternatives;

    /**
     * Returns a newly created Alternatives of parsed sequences. Must be called after enterAlternatives()!
     * @return New Alternatives object.
     */
    public Alternatives getAlternatives() {
        return new Alternatives(alternatives);
    }

    @Override
    public void enterAlternatives(final BnfParser.AlternativesContext ctx) {
        alternatives = new ArrayList<>(ctx.sequence().size());
        for (BnfParser.SequenceContext context : ctx.sequence()) {
            enterSequence(context);
            alternatives.add(sequence);
        }
    }

    @Override
    public void enterSequence(final BnfParser.SequenceContext ctx) {
        final List<Element> elements = new ArrayList<>(ctx.getChildCount());
        ElementListener listener = new ElementListener();

        for (BnfParser.ElementContext elementContext : ctx.element()) {
            listener.enterElement(elementContext);
            elements.add(listener.getElement());
        }
        sequence = new Sequence(elements);
    }
}
