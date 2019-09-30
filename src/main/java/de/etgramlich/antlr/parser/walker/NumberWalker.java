package de.etgramlich.antlr.parser.walker;

import de.etgramlich.antlr.parser.gen.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.NumberParser;

public class NumberWalker extends NumberBaseListener {

    @Override
    public void enterExpr(NumberParser.ExprContext ctx) {
        System.out.println("EXPRESSION: " + ctx.getText() + "\t has " + ctx.term().size() + " terms!");
    }

    @Override
    public void enterTerm(NumberParser.TermContext ctx) {
        System.out.println("TERM: " + ctx.getText());
    }

    @Override
    public void enterFactor(NumberParser.FactorContext ctx) {
        int number = Integer.parseInt(ctx.getText());
        System.out.println("FACTOR: " + number);
    }

}
