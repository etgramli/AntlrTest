package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.number.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.number.NumberParser;

public class TermListener extends NumberBaseListener {

    private enum Op {
        MULTIPLY, DIVIDE
    }

    private Op op = Op.MULTIPLY;

    @Override
    public void enterOperation_term(NumberParser.Operation_termContext ctx) {
        final String operation = ctx.getText();
        op = operation.trim().equals("*") ? Op.MULTIPLY : Op.DIVIDE;
    }

    private int getResult(int left, int right) {
        return op.equals(Op.MULTIPLY) ? left * right : left / right;
    }

    private int result = 0; // Default value 0, because in addition/subtraction 0 is the neutral element

    @Override
    public void enterTerm(NumberParser.TermContext ctx) {
        ctx.factor().enterRule(this);

        if (ctx.term() == null) {
            result = factor;
        } else {
            TermListener tl = new TermListener();
            ctx.term().enterRule(tl);
            int termResult = tl.getResult();

            ctx.operation_term().enterRule(this);
            result = getResult(factor, termResult);
        }
    }

    @Override
    public void exitTerm(NumberParser.TermContext ctx) {
        System.out.println("TERM: " + ctx.getText() + "\t has result: " + result);
    }

    public int getResult() {
        return result;
    }

    private int factor = 1; // Default value is 1, because in multiplication/division 1 is the neutral element

    @Override
    public void enterFactor(NumberParser.FactorContext ctx) {
        factor = Integer.parseInt(ctx.getText());
    }
}
