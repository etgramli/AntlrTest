package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AlternativeListener extends bnfBaseListener {
    private Alternative alternative;
    private final List<Alternative> alternatives = new ArrayList<>();

    @NotNull
    @Contract(pure = true)
    public List<Alternative> getAlternatives() {
        return Collections.unmodifiableList(alternatives);
    }

    @Override
    public void enterAlternatives(@NotNull bnfParser.AlternativesContext ctx) {
        for (bnfParser.AlternativeContext context : ctx.alternative()) {
            enterAlternative(context);
            alternatives.add(alternative);
        }
    }

    @Override
    public void enterAlternative(@NotNull bnfParser.AlternativeContext ctx) {
        List<Element> elements = new ArrayList<>(ctx.getChildCount());
        ElementListener listener = new ElementListener();

        for (bnfParser.ElementContext elementContext : ctx.element()) {
            listener.enterElement(elementContext);
            elements.add(listener.getElement());
        }
        alternative = new Alternative(elements);
    }

    @Override
    public void exitAlternative(@NotNull bnfParser.AlternativeContext ctx) {
        super.exitAlternative(ctx);
    }

    @Override
    public void exitAlternatives(bnfParser.AlternativesContext ctx) {
        super.exitAlternatives(ctx);
    }
}
