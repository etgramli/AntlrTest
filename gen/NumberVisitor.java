// Generated from C:/Users/Spiel/IdeaProjects/antlrtest/src/main/resources\Number.g4 by ANTLR 4.7.2
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
	 * Visit a parse tree produced by {@link NumberParser#digit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDigit(NumberParser.DigitContext ctx);
	/**
	 * Visit a parse tree produced by {@link NumberParser#positive_number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositive_number(NumberParser.Positive_numberContext ctx);
	/**
	 * Visit a parse tree produced by {@link NumberParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(NumberParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link NumberParser#operation_kind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_kind(NumberParser.Operation_kindContext ctx);
	/**
	 * Visit a parse tree produced by {@link NumberParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation(NumberParser.OperationContext ctx);
}