// Generated from C:/Users/Spiel/IdeaProjects/antlrtest/src/main/resources\Number.g4 by ANTLR 4.7.2
package de.etgramlich.antlr.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link NumberParser}.
 */
public interface NumberListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link NumberParser#digit}.
	 * @param ctx the parse tree
	 */
	void enterDigit(NumberParser.DigitContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#digit}.
	 * @param ctx the parse tree
	 */
	void exitDigit(NumberParser.DigitContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#positive_number}.
	 * @param ctx the parse tree
	 */
	void enterPositive_number(NumberParser.Positive_numberContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#positive_number}.
	 * @param ctx the parse tree
	 */
	void exitPositive_number(NumberParser.Positive_numberContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(NumberParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(NumberParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#op_plus}.
	 * @param ctx the parse tree
	 */
	void enterOp_plus(NumberParser.Op_plusContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#op_plus}.
	 * @param ctx the parse tree
	 */
	void exitOp_plus(NumberParser.Op_plusContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#op_minus}.
	 * @param ctx the parse tree
	 */
	void enterOp_minus(NumberParser.Op_minusContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#op_minus}.
	 * @param ctx the parse tree
	 */
	void exitOp_minus(NumberParser.Op_minusContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#op_multiply}.
	 * @param ctx the parse tree
	 */
	void enterOp_multiply(NumberParser.Op_multiplyContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#op_multiply}.
	 * @param ctx the parse tree
	 */
	void exitOp_multiply(NumberParser.Op_multiplyContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#op_divide}.
	 * @param ctx the parse tree
	 */
	void enterOp_divide(NumberParser.Op_divideContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#op_divide}.
	 * @param ctx the parse tree
	 */
	void exitOp_divide(NumberParser.Op_divideContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#operation_kind}.
	 * @param ctx the parse tree
	 */
	void enterOperation_kind(NumberParser.Operation_kindContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#operation_kind}.
	 * @param ctx the parse tree
	 */
	void exitOperation_kind(NumberParser.Operation_kindContext ctx);
	/**
	 * Enter a parse tree produced by {@link NumberParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation(NumberParser.OperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link NumberParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation(NumberParser.OperationContext ctx);
}