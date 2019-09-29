package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.NumberParser;

public class OperationTermListener extends NumberBaseListener {
    private enum Op {
        MULTIPLY, DIVIDE;
    }

    private Op op = Op.MULTIPLY;

    @Override
    public void enterOperation_term(NumberParser.Operation_termContext ctx) {
        final String operation = ctx.getText();
        op = operation.trim().equals("*") ? Op.MULTIPLY : Op.DIVIDE;
    }

    public int getResult(int left, int right) {
        return op.equals(Op.MULTIPLY) ? left * right : left / right;
    }
}
