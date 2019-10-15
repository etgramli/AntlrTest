package de.etgramlich.antlr.parser.listener.number;

import de.etgramlich.antlr.parser.gen.number.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.number.NumberParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TermListener extends NumberBaseListener {

    private enum Op {
        MULTIPLY, DIVIDE
    }

    private Op op = Op.MULTIPLY;
    private int factor = 1; // Default value is 1, because in multiplication/division 1 is the neutral element

    @Override
    public void enterOperation_term(@NotNull NumberParser.Operation_termContext ctx) {
        final String operation = ctx.getText();
        op = operation.trim().equals("*") ? Op.MULTIPLY : Op.DIVIDE;
    }

    @Contract(pure = true)
    private int getResult(int left, int right) {
        return op.equals(Op.MULTIPLY) ? left * right : left / right;
    }

    private int result = 0; // Default value 0, because in addition/subtraction 0 is the neutral element

    @Override
    public void enterTerm(@NotNull NumberParser.TermContext ctx) {
        ctx.factor().enterRule(this);

        if (ctx.term() == null) {
            result = factor;
        } else {
            TermListener tl = new TermListener();
            ctx.term().enterRule(tl);
            final int termResult = tl.getResult();

            ctx.operation_term().enterRule(this);
            result = getResult(factor, termResult);
        }
    }

    public int getResult() {
        return result;
    }

    @Override
    public void enterFactor(@NotNull NumberParser.FactorContext ctx) {
        factor = Integer.parseInt(ctx.getText());
    }
}
