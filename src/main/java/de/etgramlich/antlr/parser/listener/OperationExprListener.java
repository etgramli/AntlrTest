package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.NumberParser;

public class OperationExprListener extends NumberBaseListener {
    private enum Op {
        ADDITION, SUBTRACTION;
    }

    private OperationExprListener.Op op = OperationExprListener.Op.ADDITION;

    @Override
    public void enterOperation_term(NumberParser.Operation_termContext ctx) {
        final String operation = ctx.getText();
        op = operation.trim().equals("+") ? OperationExprListener.Op.ADDITION : OperationExprListener.Op.SUBTRACTION;
    }

    public int getResult(int left, int right) {
        return op.equals(OperationExprListener.Op.ADDITION) ? left + right : left - right;
    }
}
