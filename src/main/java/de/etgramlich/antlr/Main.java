package de.etgramlich.antlr;

import de.etgramlich.antlr.parser.gen.bnf.bnfLexer;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.bnf.RuleListListener;
import de.etgramlich.antlr.parser.listener.bnf.type.RuleList;
import de.etgramlich.antlr.semanticmodel.builder.InterfaceBuilder;
import de.etgramlich.antlr.semanticmodel.type.Interface;
import de.etgramlich.antlr.util.StringUtil;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class Main {
    private static final String LEXER_INPUT = "900+90+9+15*33";
    private static final Options options = new Options();
    static {
        options.addOption("t", true, "Target directory for generated sources");
        options.addOption("p", true, "Target package");
        options.addOption("g", true, "Grammar file path");
    }

    public static void main(String[] args) {
        String targetDirectory = "./";
        String grammar = StringUtils.EMPTY;
        String targetPackage = StringUtils.EMPTY;
        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            if (cmd.hasOption("t")) {
                targetDirectory = cmd.getOptionValue("t");
            }
            if (cmd.hasOption("p")) {
                targetPackage = cmd.getOptionValue("p");
            }
            if (cmd.hasOption("g")) {
                grammar = prepareGrammar(cmd.getOptionValue("g"));
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

        bnfLexer lexer = new bnfLexer(CharStreams.fromString(grammar));
        bnfParser parser = new bnfParser(new CommonTokenStream(lexer));

        RuleListListener listener = new RuleListListener();
        parser.rulelist().enterRule(listener);
        RuleList ruleList = listener.getRuleList();
        ruleList.saveInterfaces(targetDirectory, targetPackage);

        Interface iface = InterfaceBuilder.Interface("ifac")
                .method()
                    .name("Getter_1")
                    .returnType("String")
                .method()
                    .name("Setter 1")
                    .argument()
                        .setName("newVal")
                        .setType("String")
                    .returnType("void")
        .build();
        System.out.println(iface);
    }

    @NotNull
    private static String prepareGrammar(final String filepath) throws IOException {
        final List<String> grammar = Files.readAllLines(Paths.get(filepath));
        final int beginIndex = grammar.indexOf(
                grammar.stream().filter(i -> i.matches("grammar .*;")).findFirst().orElse(null)
        );
        List<String> rules = grammar.subList(beginIndex + 1, grammar.size() - 1);
        rules = StringUtil.removeDuplicates(StringUtil.stripBlankLines(rules));
        return String.join("\n", rules);
    }
}
