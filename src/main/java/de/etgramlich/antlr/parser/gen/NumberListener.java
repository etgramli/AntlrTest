// Generated from C:/Users/Spiel/IdeaProjects/antlrtest/src/main/resources\Number.g4 by ANTLR 4.7.2
package de.etgramlich.antlr.parser.gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link NumberParser}.
 */
public interface NumberListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link NumberParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(NumberParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(NumberParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#operation_expr}.
	 * @param ctx the parse tree
	 */
	void enterOperation_expr(NumberParser.Operation_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#operation_expr}.
	 * @param ctx the parse tree
	 */
	void exitOperation_expr(NumberParser.Operation_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(NumberParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(NumberParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#operation_term}.
	 * @param ctx the parse tree
	 */
	void enterOperation_term(NumberParser.Operation_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#operation_term}.
	 * @param ctx the parse tree
	 */
	void exitOperation_term(NumberParser.Operation_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(NumberParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(NumberParser.FactorContext ctx);
}