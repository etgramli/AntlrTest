package de.etgramlich.antlr.parser.visitor;

import de.etgramlich.antlr.parser.gen.NumberBaseVisitor;
import de.etgramlich.antlr.parser.gen.NumberParser;

public class TermVisitor extends NumberBaseVisitor<Integer> {
    @Override
    public Integer visitTerm(NumberParser.TermContext ctx) {
        assert(ctx.children.size() == 1 || ctx.children.size() == 3);

        FactorVisitor fv = new FactorVisitor();
        int factorValue = fv.visitFactor(ctx.right);

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
}
