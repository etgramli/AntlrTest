package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.type.Element;
import org.jetbrains.annotations.NotNull;

/**
 * Parses an element of ANTL4's bnf grammar.
 * Currently only allows text and id options.
 */
public final class ElementListener extends BnfBaseListener {
    private Element element;

    @Override
    public void enterElement(@NotNull BnfParser.ElementContext ctx)  {
        if (ctx.nt() != null || ctx.keyword() != null || ctx.type() != null) {
            NonTerminalListener listener = new NonTerminalListener();
            if (ctx.nt() != null) {
                listener.enterNt(ctx.nt());
                element = listener.getNonTerminal();
            } else if (ctx.type() != null) {
                listener.enterType(ctx.type());
                element = listener.getType();
            } else {
                listener.enterKeyword(ctx.keyword());
                element = listener.getKeyword();
            }
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

    @Override
    public void exitElement(BnfParser.ElementContext ctx) {
        super.exitElement(ctx);
    }

    public Element getElement() {
        return element;
    }
}
