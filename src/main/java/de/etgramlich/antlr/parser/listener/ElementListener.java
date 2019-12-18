package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.bnf.BnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.BnfParser;
import de.etgramlich.antlr.parser.type.rhstype.Element;
import org.jetbrains.annotations.Contract;
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
            IdListener listener = new IdListener();
            if (ctx.nt() != null) {
                listener.enterNt(ctx.nt());
            } else if (ctx.type() != null) {
                listener.enterType(ctx.type());
            } else {
                listener.enterKeyword(ctx.keyword());
            }
            element = new Element(listener.getId());
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
            element = new Element(repetitionListener.getRepetition());
        }
    }

    @Override
    public void exitElement(BnfParser.ElementContext ctx) {
        super.exitElement(ctx);
    }

    @Contract(pure = true)
    public Element getElement() {
        return element;
    }
}
