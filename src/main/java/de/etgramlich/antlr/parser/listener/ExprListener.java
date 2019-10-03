package de.etgramlich.antlr.parser.listener;

import de.etgramlich.antlr.parser.gen.NumberBaseListener;
import de.etgramlich.antlr.parser.gen.NumberParser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExprListener extends NumberBaseListener {

    // Has no default value, because it is the uppermost node and not used recursively
    private Optional<Integer> result = Optional.empty();

    @Override
    public void enterExpr(NumberParser.ExprContext ctx) {
        TermListener tl = new TermListener();
        ctx.term().enterRule(tl);
        int termResult = tl.getResult();

        if (ctx.expr() == null) {
            result = Optional.of(termResult);
            return;
        }

        ExprListener el = new ExprListener();
        ctx.expr().enterRule(el);
        int exprResult = el.getResult().get();

        OperationExprListener oel = new OperationExprListener();
        ctx.operation_expr().enterRule(oel);
        result = Optional.of(oel.getResult(termResult, exprResult));
    }

    @Override
    public void exitExpr(NumberParser.ExprContext ctx) {
        System.out.println("EXPRESSION: " + ctx.getText() + "\t has result: " + result);
    }

    public Optional<Integer> getResult() {
        return result;
    }
}
