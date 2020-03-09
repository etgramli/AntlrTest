package de.etgramlich.dsl;

import de.etgramlich.dsl.graph.ForestBuilder;
import de.etgramlich.dsl.parser.gen.bnf.BnfLexer;
import de.etgramlich.dsl.parser.gen.bnf.BnfParser;
import de.etgramlich.dsl.parser.listener.BnfListener;
import de.etgramlich.dsl.parser.type.Bnf;
import de.etgramlich.dsl.generator.InterfaceBuilder;
import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Main program that converts a EBNF grammar to Java interfaces.
 */
public final class Main {
    private Main() { }

    /**
     * A regex string matching a non-terminal of the grammar.
     */
    private static final String NON_TERMINAL_REGEX = "<[a-zA-Z]+>";

    /**
     * Stores command line options.
     */
    private static final Options OPTIONS = new Options();
    static {
        OPTIONS.addOption("h", "help", false, "Prints this help text.");
        OPTIONS.addRequiredOption("d", "directory", true, "Target directory for generated sources");
        OPTIONS.addRequiredOption("p", "package", true, "Target package");
        OPTIONS.addRequiredOption("g", "grammar", true, "Grammar file path");
    }

    /**
     * Reads grammar from file and outputs java interfaces in the corresponding package directory in the provided
     * target directory.
     * @param args Command line arguments to be parsed. Should not be empty.
     */
    public static void main(final String[] args) {
        final String grammar;
        final String targetDirectory;
        final String targetPackage;
        try {
            final CommandLine cmd = new DefaultParser().parse(OPTIONS, args);
            targetDirectory = cmd.getOptionValue("d");
            targetPackage = cmd.getOptionValue("p");
            grammar = prepareGrammar(cmd.getOptionValue("g"));
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.println("Could not parse CMD arguments! Run with -h for help.");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Grammar file could not be read!");
            return;
        }

        final BnfParser parser = new BnfParser(new CommonTokenStream(new BnfLexer(CharStreams.fromString(grammar))));
        final BnfListener listener = new BnfListener();
        listener.enterBnf(parser.bnf());
        final Bnf bnf = listener.getBnf();

        final BnfRuleGraph graph = new ForestBuilder(bnf).getMergedGraph();

        try {
            graph.renderBnfRuleGraph(targetDirectory + File.separator + "graph.gv");

            new InterfaceBuilder(targetDirectory, targetPackage).saveInterfaces(graph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String prepareGrammar(final String filepath) throws IOException {
        return Files.readAllLines(Paths.get(filepath)).stream()
                .dropWhile(line -> !line.matches(NON_TERMINAL_REGEX))
                .filter(line -> !StringUtils.isBlank(line))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
