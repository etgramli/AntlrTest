package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.Element;
import de.etgramlich.parser.type.Sequence;

import java.util.ArrayList;
import java.util.List;

public final class AlternativeListener extends BnfBaseListener {
    private Sequence sequence;
    private final List<Sequence> alternatives = new ArrayList<>();

    public Alternatives getAlternatives() {
        return new Alternatives(alternatives);
    }

    @Override
    public void enterAlternatives(BnfParser.AlternativesContext ctx) {
        for (BnfParser.SequenceContext context : ctx.sequence()) {
            enterSequence(context);
            alternatives.add(sequence);
        }
    }

    @Override
    public void enterSequence(BnfParser.SequenceContext ctx) {
        List<Element> elements = new ArrayList<>(ctx.getChildCount());
        ElementListener listener = new ElementListener();

        for (BnfParser.ElementContext elementContext : ctx.element()) {
            listener.enterElement(elementContext);
            elements.add(listener.getElement());
        }
        sequence = new Sequence(elements);
    }

    @Override
    public void exitSequence(BnfParser.SequenceContext ctx) {
        super.exitSequence(ctx);
    }

    @Override
    public void exitAlternatives(BnfParser.AlternativesContext ctx) {
        super.exitAlternatives(ctx);
    }
}
