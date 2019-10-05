package de.etgramlich.antlr;

import de.etgramlich.antlr.parser.gen.NumberLexer;
import de.etgramlich.antlr.parser.gen.NumberParser;
import de.etgramlich.antlr.parser.listener.ExprListener;
import de.etgramlich.antlr.parser.visitor.NumberVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public final class Main {
    private static final String LEXER_INPUT = "999+15*33";

    public static void main(String[] args) {
        NumberLexer lexer = new NumberLexer(CharStreams.fromString(LEXER_INPUT));
        NumberParser parser = new NumberParser(new CommonTokenStream(lexer));

        ExprListener listener = new ExprListener();
        parser.expr().enterRule(listener);
        if (listener.getResult().isEmpty()) {
            System.err.println("Error parsing expression!!! " + LEXER_INPUT);
        } else {
            System.out.println("Result: " + listener.getResult().get());
        }

        lexer = new NumberLexer(CharStreams.fromString(LEXER_INPUT));
        parser = new NumberParser(new CommonTokenStream(lexer));
        System.out.println("Vistor result: " + new NumberVisitor().visitExpr(parser.expr()));
    }
}
