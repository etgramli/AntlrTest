package de.etgramlich.antlr.parser.visitor;

import de.etgramlich.antlr.parser.gen.number.NumberBaseVisitor;
import de.etgramlich.antlr.parser.gen.number.NumberParser;

public class NumberVisitor extends NumberBaseVisitor<Integer> {

    @Override
    public Integer visitExpr(NumberParser.ExprContext ctx) {
        assert(ctx.children.size() == 1 || ctx.children.size() == 3);

        int termValue = visitTerm(ctx.right);

        if (ctx.children.size() == 1) {
            return termValue;
        }

        int exprValue = visitExpr(ctx.left);

        switch (ctx.operation_expr().getText()) {
            case "+":
                return exprValue + termValue;
            case "-":
                return exprValue - termValue;
            default:    // Should never happen (due to grammar)
                return 1;
        }
    }

    @Override
    public Integer visitTerm(NumberParser.TermContext ctx) {
        assert(ctx.children.size() == 1 || ctx.children.size() == 3);

        int factorValue = visitFactor(ctx.right);

        if (ctx.children.size() == 1) {
            return factorValue;
        }

        int termValue = visitTerm(ctx.left);

        switch (ctx.operation_term().getText()) {
            case "*":
                return termValue * factorValue;
            case "/":
                return termValue / factorValue;
            default:    // Should never happen (due to grammar)
                return 1;
        }
    }

    @Override
    public Integer visitFactor(NumberParser.FactorContext ctx) {
        return Integer.valueOf(ctx.getText());
    }
}
