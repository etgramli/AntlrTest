// Generated from C:/Users/Spiel/IdeaProjects/antlrtest/src/main/resources\Number.g4 by ANTLR 4.7.2
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