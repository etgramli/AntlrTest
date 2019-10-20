package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.type.Alternative;
import de.etgramlich.antlr.parser.listener.bnf.type.Element;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AlternativeListener extends bnfBaseListener {
    private Alternative alternative;

    @Override
    public void enterAlternative(@NotNull bnfParser.AlternativeContext ctx) {
        List<Element> elements = new ArrayList<>(ctx.getChildCount());
        ElementListener listener = new ElementListener();

        for (bnfParser.ElementContext elementContext : ctx.element()) {
            listener.enterElement(elementContext);
            elements.add(listener.getElement());
        }
        this.alternative = new Alternative(elements);
    }

    public Alternative getAlternative() {
        return alternative;
    }
}
