package de.etgramlich.antlr;

import de.etgramlich.antlr.parser.gen.number.NumberLexer;
import de.etgramlich.antlr.parser.gen.number.NumberParser;
import de.etgramlich.antlr.parser.listener.number.ExprListener;
import de.etgramlich.antlr.parser.listener.number.ExprToXML;
import de.etgramlich.antlr.parser.visitor.NumberVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Main {
    private static final String LEXER_INPUT = "900+90+9+15*33";

    @NotNull
    @Contract(" -> new")
    private static NumberParser newNumberParser() {
        return new NumberParser(new CommonTokenStream(new NumberLexer(CharStreams.fromString(LEXER_INPUT))));
    }

    public static void main(String[] args) {
        NumberParser parser = newNumberParser();

        ExprListener listener = new ExprListener();
        parser.expr().enterRule(listener);
        if (listener.getResult().isEmpty()) {
            System.err.println("Error parsing expression!!! " + LEXER_INPUT);
        } else {
            System.out.println("Result: " + listener.getResult().get());
        }

        parser = newNumberParser();
        System.out.println("Visitor result: " + new NumberVisitor().visitExpr(parser.expr()));

        parser = newNumberParser();
        ExprToXML toXML = new ExprToXML("./out.xml");
        parser.expression().exitRule(toXML);
        System.out.println(toXML.getXml().get(parser.expr()));
    }
}
