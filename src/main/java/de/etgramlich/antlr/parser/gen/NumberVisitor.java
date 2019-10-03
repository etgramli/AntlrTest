// Generated from /home/eti/IdeaProjects/AntlrTest/src/main/resources/Number.g4 by ANTLR 4.7.2
package de.etgramlich.antlr.parser.gen;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link NumberParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface NumberVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link NumberParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(NumberParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link NumberParser#operation_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_expr(NumberParser.Operation_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link NumberParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(NumberParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link NumberParser#operation_term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_term(NumberParser.Operation_termContext ctx);
	/**
	 * Visit a parse tree produced by {@link NumberParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(NumberParser.FactorContext ctx);
}