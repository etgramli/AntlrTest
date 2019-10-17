// Generated from /home/eti/IdeaProjects/AntlrTest/src/main/resources/bnf.g4 by ANTLR 4.7.2
package de.etgramlich.antlr.parser.gen.bnf;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link bnfParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface bnfVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link bnfParser#rulelist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRulelist(bnfParser.RulelistContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#rule_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRule_(bnfParser.Rule_Context ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#lhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLhs(bnfParser.LhsContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#rhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRhs(bnfParser.RhsContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#alternatives}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlternatives(bnfParser.AlternativesContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#alternative}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlternative(bnfParser.AlternativeContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(bnfParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#optional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptional(bnfParser.OptionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#zeroormore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZeroormore(bnfParser.ZeroormoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#oneormore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOneormore(bnfParser.OneormoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(bnfParser.TextContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(bnfParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link bnfParser#ruleid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleid(bnfParser.RuleidContext ctx);
}