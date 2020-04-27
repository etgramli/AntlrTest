package de.etgramlich.dsl.parser.listener;

import de.etgramlich.dsl.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.dsl.parser.gen.bnf.BnfParser;
import de.etgramlich.dsl.parser.type.Element;

/**
 * Parses an element of ANTL4's bnf grammar.
 * Call sequence:
 * 1. new ElementListener()
 * 2. enterElement()
 * 3. getElement()
 */
public final class ElementListener extends BnfBaseListener {
    /**
     * BNF element.
     */
    private Element element;

    /**
     * Queries the parsed bnf element. Must be called after enterElement()!
     * @return New Element object after call to enterElement(), else null.
     */
    public Element getElement() {
        return element;
    }

    @Override
    public void enterElement(final BnfParser.ElementContext ctx)  {
        if (ctx.nt() != null || ctx.keyword() != null || ctx.type() != null) {
            NonTerminalListener listener = new NonTerminalListener();
            if (ctx.nt() != null) {
                listener.enterNt(ctx.nt());
            } else if (ctx.type() != null) {
                listener.enterType(ctx.type());
            } else {
                listener.enterKeyword(ctx.keyword());
            }
            element = listener.getTextElement();
        } else if (ctx.children.size() == 1 && ctx.children.get(0).getText().equals("::=")) {
            System.out.println("Assignment - skipping!!!");
        } else {
            RepetitionListener repetitionListener = new RepetitionListener();
            if (ctx.optional() != null) {
                repetitionListener.enterOptional(ctx.optional());
            } else if (ctx.zeroormore() != null) {
                repetitionListener.enterZeroormore(ctx.zeroormore());
            } else if (ctx.precedence() != null) {
                repetitionListener.enterPrecedence(ctx.precedence());
            } else {
                throw new UnsupportedOperationException("Element type not recognized!!! (" + ctx.getText() + ")");
            }
            element = repetitionListener.getRepetition();
        }
    }
}
