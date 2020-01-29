// Generated from /home/eti/IdeaProjects/AntlrTest/src/main/resources/Bnf.g4 by ANTLR 4.8
package de.etgramlich.parser.gen.bnf;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BnfParser}.
 */
public interface BnfListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BnfParser#bnf}.
	 * @param ctx the parse tree
	 */
	void enterBnf(BnfParser.BnfContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#bnf}.
	 * @param ctx the parse tree
	 */
	void exitBnf(BnfParser.BnfContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#bnfrule}.
	 * @param ctx the parse tree
	 */
	void enterBnfrule(BnfParser.BnfruleContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#bnfrule}.
	 * @param ctx the parse tree
	 */
	void exitBnfrule(BnfParser.BnfruleContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#lhs}.
	 * @param ctx the parse tree
	 */
	void enterLhs(BnfParser.LhsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#lhs}.
	 * @param ctx the parse tree
	 */
	void exitLhs(BnfParser.LhsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#rhs}.
	 * @param ctx the parse tree
	 */
	void enterRhs(BnfParser.RhsContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#rhs}.
	 * @param ctx the parse tree
	 */
	void exitRhs(BnfParser.RhsContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#alternatives}.
	 * @param ctx the parse tree
	 */
	void enterAlternatives(BnfParser.AlternativesContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#alternatives}.
	 * @param ctx the parse tree
	 */
	void exitAlternatives(BnfParser.AlternativesContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#sequence}.
	 * @param ctx the parse tree
	 */
	void enterSequence(BnfParser.SequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#sequence}.
	 * @param ctx the parse tree
	 */
	void exitSequence(BnfParser.SequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(BnfParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(BnfParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#optional}.
	 * @param ctx the parse tree
	 */
	void enterOptional(BnfParser.OptionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#optional}.
	 * @param ctx the parse tree
	 */
	void exitOptional(BnfParser.OptionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#zeroormore}.
	 * @param ctx the parse tree
	 */
	void enterZeroormore(BnfParser.ZeroormoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#zeroormore}.
	 * @param ctx the parse tree
	 */
	void exitZeroormore(BnfParser.ZeroormoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#precedence}.
	 * @param ctx the parse tree
	 */
	void enterPrecedence(BnfParser.PrecedenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#precedence}.
	 * @param ctx the parse tree
	 */
	void exitPrecedence(BnfParser.PrecedenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#nt}.
	 * @param ctx the parse tree
	 */
	void enterNt(BnfParser.NtContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#nt}.
	 * @param ctx the parse tree
	 */
	void exitNt(BnfParser.NtContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(BnfParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(BnfParser.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link BnfParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(BnfParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link BnfParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(BnfParser.TypeContext ctx);
}