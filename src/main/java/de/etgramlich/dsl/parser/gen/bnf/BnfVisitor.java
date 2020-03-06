// Generated from /home/eti/IdeaProjects/AntlrTest/src/main/resources/Bnf.g4 by ANTLR 4.8
package de.etgramlich.dsl.parser.gen.bnf;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link BnfParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface BnfVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link BnfParser#bnf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBnf(BnfParser.BnfContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#bnfrule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBnfrule(BnfParser.BnfruleContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#lhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLhs(BnfParser.LhsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#rhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRhs(BnfParser.RhsContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#alternatives}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlternatives(BnfParser.AlternativesContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#sequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence(BnfParser.SequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(BnfParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#optional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptional(BnfParser.OptionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#zeroormore}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitZeroormore(BnfParser.ZeroormoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#precedence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrecedence(BnfParser.PrecedenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#nt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNt(BnfParser.NtContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(BnfParser.KeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link BnfParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(BnfParser.TypeContext ctx);
}