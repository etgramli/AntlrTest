package de.etgramlich.antlr;

import de.etgramlich.antlr.parser.NumberLexer;
import de.etgramlich.antlr.parser.NumberParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public final class Main {
    private static final String LEXER_INPUT = "999 + 15";

    public static void main(String[] args) {
        NumberLexer lexer = new NumberLexer(CharStreams.fromString(LEXER_INPUT));
        NumberParser parser = new NumberParser(new CommonTokenStream(lexer));

        String name = parser.number().getText();

        System.out.println(name);
    }
}
