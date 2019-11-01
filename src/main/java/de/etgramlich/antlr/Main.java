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
import org.apache.commons.cli.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public final class Main {
    private static final String LEXER_INPUT = "900+90+9+15*33";

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("t", true, "Target directory for generated sources");
        options.addOption("g", true, "Grammar file path");
        CommandLineParser cliParser = new DefaultParser();
        String targetDirectory = "./";
        final String grammarFilePath;
        List<String> grammar = Collections.emptyList();
        try {
            CommandLine cmd = cliParser.parse(options, args);
            if (cmd.hasOption("t")) {
                targetDirectory = cmd.getOptionValue("t");
            }
            if (cmd.hasOption("g")) {
                grammarFilePath = cmd.getOptionValue("g");
                grammar = Files.readAllLines(Paths.get(grammarFilePath));
                for (String line : grammar) {
                    if (line.matches("grammar .*;")) {
                        grammar = grammar.subList(grammar.indexOf(line)+1, grammar.size()-1);
                        break;
                    }
                }
            } else {
                System.err.println("No grammar file given!!!");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Grammar file could not be read!");
            return;
        }

        bnfLexer lexer = new bnfLexer(CharStreams.fromString(String.join("\n", grammar)));
        bnfParser parser = new bnfParser(new CommonTokenStream(lexer));

        RuleListListener listener = new RuleListListener();
        parser.rulelist().enterRule(listener);
        RuleList ruleList = listener.getRuleList();
        ruleList.saveInterfaces(targetDirectory);
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
