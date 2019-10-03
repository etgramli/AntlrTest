package de.etgramlich.antlr.parser;

import de.etgramlich.antlr.parser.gen.NumberListener;
import de.etgramlich.antlr.parser.gen.NumberParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class TestNumberListener implements NumberListener {
    @Override
    public void enterExpr(NumberParser.ExprContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void exitExpr(NumberParser.ExprContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void enterOperation_expr(NumberParser.Operation_exprContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void exitOperation_expr(NumberParser.Operation_exprContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void enterTerm(NumberParser.TermContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void exitTerm(NumberParser.TermContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void enterOperation_term(NumberParser.Operation_termContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void exitOperation_term(NumberParser.Operation_termContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void enterFactor(NumberParser.FactorContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void exitFactor(NumberParser.FactorContext ctx) {
        System.out.println(ctx);
    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {
        System.out.println(terminalNode);
    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {
        System.out.println(errorNode);
    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {
        System.out.println(parserRuleContext);
    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {
        System.out.println(parserRuleContext);
    }
}
