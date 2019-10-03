package de.etgramlich.antlr.parser.visitor;

import de.etgramlich.antlr.parser.gen.NumberBaseVisitor;
import de.etgramlich.antlr.parser.gen.NumberParser;

public class ExprVisitor extends NumberBaseVisitor<Integer> {
    @Override
    public Integer visitExpr(NumberParser.ExprContext ctx) {
        assert(ctx.children.size() == 1 || ctx.children.size() == 3);

        TermVisitor tv = new TermVisitor();
        int termValue = tv.visitTerm(ctx.right);

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
}
