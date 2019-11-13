// Generated from /home/eti/IdeaProjects/AntlrTest/src/main/resources/bnf.g4 by ANTLR 4.7.2
package de.etgramlich.antlr.parser.gen.bnf;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link bnfParser}.
 */
public interface bnfListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link bnfParser#rulelist}.
	 * @param ctx the parse tree
	 */
	void enterRulelist(bnfParser.RulelistContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#rulelist}.
	 * @param ctx the parse tree
	 */
	void exitRulelist(bnfParser.RulelistContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#rule_}.
	 * @param ctx the parse tree
	 */
	void enterRule_(bnfParser.Rule_Context ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#rule_}.
	 * @param ctx the parse tree
	 */
	void exitRule_(bnfParser.Rule_Context ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#lhs}.
	 * @param ctx the parse tree
	 */
	void enterLhs(bnfParser.LhsContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#lhs}.
	 * @param ctx the parse tree
	 */
	void exitLhs(bnfParser.LhsContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#rhs}.
	 * @param ctx the parse tree
	 */
	void enterRhs(bnfParser.RhsContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#rhs}.
	 * @param ctx the parse tree
	 */
	void exitRhs(bnfParser.RhsContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#alternatives}.
	 * @param ctx the parse tree
	 */
	void enterAlternatives(bnfParser.AlternativesContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#alternatives}.
	 * @param ctx the parse tree
	 */
	void exitAlternatives(bnfParser.AlternativesContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#alternative}.
	 * @param ctx the parse tree
	 */
	void enterAlternative(bnfParser.AlternativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#alternative}.
	 * @param ctx the parse tree
	 */
	void exitAlternative(bnfParser.AlternativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(bnfParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(bnfParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#optional}.
	 * @param ctx the parse tree
	 */
	void enterOptional(bnfParser.OptionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#optional}.
	 * @param ctx the parse tree
	 */
	void exitOptional(bnfParser.OptionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#zeroormore}.
	 * @param ctx the parse tree
	 */
	void enterZeroormore(bnfParser.ZeroormoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#zeroormore}.
	 * @param ctx the parse tree
	 */
	void exitZeroormore(bnfParser.ZeroormoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#precedence}.
	 * @param ctx the parse tree
	 */
	void enterPrecedence(bnfParser.PrecedenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#precedence}.
	 * @param ctx the parse tree
	 */
	void exitPrecedence(bnfParser.PrecedenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#text}.
	 * @param ctx the parse tree
	 */
	void enterText(bnfParser.TextContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#text}.
	 * @param ctx the parse tree
	 */
	void exitText(bnfParser.TextContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(bnfParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(bnfParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#ruleid}.
	 * @param ctx the parse tree
	 */
	void enterRuleid(bnfParser.RuleidContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#ruleid}.
	 * @param ctx the parse tree
	 */
	void exitRuleid(bnfParser.RuleidContext ctx);
	/**
	 * Enter a parse tree produced by {@link bnfParser#letterrange}.
	 * @param ctx the parse tree
	 */
	void enterLetterrange(bnfParser.LetterrangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link bnfParser#letterrange}.
	 * @param ctx the parse tree
	 */
	void exitLetterrange(bnfParser.LetterrangeContext ctx);
}