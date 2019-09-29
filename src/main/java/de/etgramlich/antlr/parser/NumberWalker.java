package de.etgramlich.antlr.parser;

import de.etgramlich.antlr.parser.gen.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.NumberParser;

public class NumberWalker extends NumberBaseListener {

    @Override
    public void enterExpr(NumberParser.ExprContext ctx) {
        System.out.println("EXPRESSION: " + ctx.getText());
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
