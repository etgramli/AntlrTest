package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.number.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.number.NumberParser;

import java.util.Optional;

public class ExprListener extends NumberBaseListener {
    private static final int EXPR_NEUTRAL_ELEMENT = 0;

    private enum Op {
        ADDITION, SUBTRACTION;
    }

    private Op op = Op.ADDITION;

    @Override
    public void enterOperation_term(NumberParser.Operation_termContext ctx) {
        final String operation = ctx.getText();
        op = operation.trim().equals("+") ? Op.ADDITION : Op.SUBTRACTION;
    }

    private int getResult(int left, int right) {
        return op.equals(Op.ADDITION) ? left + right : left - right;
    }

    // Has no default value, because it is the uppermost node and not used recursively
    private Optional<Integer> result = Optional.empty();

    @Override
    public void enterExpr(NumberParser.ExprContext ctx) {
        TermListener tl = new TermListener();
        ctx.term().enterRule(tl);
        int termResult = tl.getResult();

        if (ctx.expr() == null) {
            result = Optional.of(termResult);
        } else {
            ExprListener el = new ExprListener();
            ctx.expr().enterRule(el);
            int exprValue = el.getResult().orElse(EXPR_NEUTRAL_ELEMENT);

            ctx.operation_expr().enterRule(this);
            result = Optional.of(getResult(termResult, exprValue));
        }
    }

    @Override
    public void exitExpr(NumberParser.ExprContext ctx) {
        System.out.println("EXPRESSION: " + ctx.getText() + "\t has result: " + result);
    }

    public Optional<Integer> getResult() {
        return result;
    }
}
