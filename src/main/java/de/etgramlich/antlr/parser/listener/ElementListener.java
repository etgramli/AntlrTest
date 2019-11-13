package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.bnf.bnfBaseListener;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.type.rhstype.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Parses an element of ANTL4's bnf grammar.
 * Currently only allows text and id options.
 */
public final class ElementListener extends bnfBaseListener {
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
        } else if (ctx.letterrange() != null) {
            LetterrangeListener listener = new LetterrangeListener();
            listener.enterLetterrange(ctx.letterrange());
            element = new Element(listener.getLetterRange());
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
    public void exitElement(bnfParser.ElementContext ctx) {
        super.exitElement(ctx);
    }

    @Contract(pure = true)
    public Element getElement() {
        return element;
    }
}
