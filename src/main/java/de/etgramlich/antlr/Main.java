package de.etgramlich.antlr;

import de.etgramlich.antlr.parser.gen.bnf.bnfLexer;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.gen.number.NumberLexer;
import de.etgramlich.antlr.parser.gen.number.NumberParser;
import de.etgramlich.antlr.parser.listener.bnf.RuleListListener;
import de.etgramlich.antlr.parser.listener.bnf.type.RuleList;
import de.etgramlich.antlr.parser.listener.number.ExprListener;
import de.etgramlich.antlr.parser.visitor.NumberVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Main {
    private static final String LEXER_INPUT = "900+90+9+15*33";

    public static void main(String[] args) {
        String numberGrammar = StringUtils.EMPTY;
        try {
            numberGrammar = Files.readString(Paths.get("src/main/resources/expressionGrammar.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bnfLexer lexer = new bnfLexer(CharStreams.fromString(numberGrammar));
        bnfParser parser = new bnfParser(new CommonTokenStream(lexer));

        RuleListListener listener = new RuleListListener();
        parser.rulelist().enterRule(listener);
        RuleList ruleList = listener.getRuleList();
        System.out.println(ruleList);
    }


    @NotNull
    @Contract(" -> new")
    private static NumberParser newNumberParser() {
        return new NumberParser(new CommonTokenStream(new NumberLexer(CharStreams.fromString(LEXER_INPUT))));
    }

    private static void parseExpression() {
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
    }
}
