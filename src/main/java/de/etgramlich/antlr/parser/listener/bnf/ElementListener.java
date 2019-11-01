package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.repetition.RepetitionListener;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.Element;
import org.jetbrains.annotations.NotNull;

/**
 * Parses an element of ANTL4's bnf grammar.
 * Currently only allows text and id options.
 */
public class ElementListener extends bnfBaseListener {
    private Element element;

    @Override
    public void enterElement(@NotNull bnfParser.ElementContext ctx)  {
        if (ctx.id() != null || ctx.text() != null) {
            IdListener listener = new IdListener();
            if (ctx.id() != null) {
                listener.enterId(ctx.id());
            } else {
                listener.enterText(ctx.text());
            }
            element = new Element(listener.getId());
        } else {
            RepetitionListener repetitionListener = new RepetitionListener();
            if (ctx.optional() != null) {
                repetitionListener.enterOptional(ctx.optional());
            } else if (ctx.zeroormore() != null) {
                repetitionListener.enterZeroormore(ctx.zeroormore());
            } else if (ctx.oneormore() != null) {
                repetitionListener.enterOneormore(ctx.oneormore());
            } else if (ctx.children.size() == 1 && ctx.children.get(0).getText().equals("::=")){
                System.out.println("Assignment!!!");
            } else {
                throw new UnsupportedOperationException("Element type not recognized!!!");
            }
            element = new Element(repetitionListener.getAlternatives());
        }
    }

    public Element getElement() {
        return element;
    }
}
