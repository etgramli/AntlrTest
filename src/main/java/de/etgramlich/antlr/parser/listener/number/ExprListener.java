package de.etgramlich.antlr.parser.listener.number;

import de.etgramlich.antlr.parser.gen.number.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.number.NumberParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ExprListener extends NumberBaseListener {
    private static final int EXPR_NEUTRAL_ELEMENT = 0;

    private enum Op {
        ADDITION, SUBTRACTION;
    }

    private Op op = Op.ADDITION;

    @Override
    public void enterOperation_expr(@NotNull NumberParser.Operation_exprContext ctx) {
        final String operation = ctx.getText();
        op = operation.trim().equals("+") ? Op.ADDITION : Op.SUBTRACTION;
    }

    @Contract(pure = true)
    private int getResult(int left, int right) {
        return op.equals(Op.ADDITION) ? left + right : left - right;
    }

    // Has no default value, because it is the uppermost node and not used recursively
    private Optional<Integer> result = Optional.empty();

    @Override
    public void enterExpr(@NotNull NumberParser.ExprContext ctx) {
        TermListener tl = new TermListener();
        ctx.term().enterRule(tl);
        final int termResult = tl.getResult();

        if (ctx.expr() == null) {
            result = Optional.of(termResult);
        } else {
            ExprListener el = new ExprListener();
            ctx.expr().enterRule(el);
            final int exprValue = el.getResult().orElse(EXPR_NEUTRAL_ELEMENT);

            ctx.operation_expr().enterRule(this);
            result = Optional.of(getResult(termResult, exprValue));
        }
    }

    public Optional<Integer> getResult() {
        return result;
    }
}
