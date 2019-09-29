package de.etgramlich.antlr;

import de.etgramlich.antlr.parser.NumberWalker;
import de.etgramlich.antlr.parser.TestNumberListener;
import de.etgramlich.antlr.parser.gen.NumberLexer;
import de.etgramlich.antlr.parser.gen.NumberParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public final class Main {
    private static final String LEXER_INPUT = "999+15";

    public static void main(String[] args) {
        NumberLexer lexer = new NumberLexer(CharStreams.fromString(LEXER_INPUT));
        NumberParser parser = new NumberParser(new CommonTokenStream(lexer));
        parser.addParseListener(new TestNumberListener());

        NumberParser.ExprContext ctx = parser.expr();
        ParseTreeWalker.DEFAULT.walk(new NumberWalker(), ctx);
    }
}
