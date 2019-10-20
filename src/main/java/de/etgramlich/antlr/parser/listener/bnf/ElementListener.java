package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.type.Element;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Parses an element of ANTL4's bnf grammar.
 * Currently only allows text and id options.
 */
public class ElementListener extends bnfBaseListener {
    private Element element;

    @Override
    public void enterElement(@NotNull bnfParser.ElementContext ctx)  {
        IdListener listener = new IdListener();
        if (ctx.id() != null) {
            listener.enterId(ctx.id());
            element = new Element(listener.getId());
        } else if (ctx.text() != null) {
            listener.enterText(ctx.text());
            element = new Element(listener.getText());
        } else {
            // ToDo
            throw new UnsupportedOperationException("Elements of optional, ueroormore and oneormore are not implemented yet!");
        }
    }

    public Element getElement() {
        return element;
    }
}
