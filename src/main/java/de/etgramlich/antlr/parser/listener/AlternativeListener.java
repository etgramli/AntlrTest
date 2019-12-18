package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.BnfParser;
import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.rhstype.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AlternativeListener extends BnfBaseListener {
    private Alternative alternative;
    private final List<Alternative> alternatives = new ArrayList<>();

    @NotNull
    @Contract(pure = true)
    public List<Alternative> getAlternatives() {
        return Collections.unmodifiableList(alternatives);
    }

    @Override
    public void enterAlternatives(@NotNull BnfParser.AlternativesContext ctx) {
        for (BnfParser.SequenceContext context : ctx.sequence()) {
            enterSequence(context);
            alternatives.add(alternative);
        }
    }

    @Override
    public void enterSequence(@NotNull BnfParser.SequenceContext ctx) {
        List<Element> elements = new ArrayList<>(ctx.getChildCount());
        ElementListener listener = new ElementListener();

        for (BnfParser.ElementContext elementContext : ctx.element()) {
            listener.enterElement(elementContext);
            elements.add(listener.getElement());
        }
        alternative = new Alternative(elements);
    }

    @Override
    public void exitSequence(@NotNull BnfParser.SequenceContext ctx) {
        super.exitSequence(ctx);
    }

    @Override
    public void exitAlternatives(BnfParser.AlternativesContext ctx) {
        super.exitAlternatives(ctx);
    }
}
