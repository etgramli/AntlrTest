package de.etgramlich.dsl;

import de.etgramlich.dsl.graph.ForestBuilder;
import de.etgramlich.dsl.parser.gen.bnf.BnfLexer;
import de.etgramlich.dsl.parser.gen.bnf.BnfParser;
import de.etgramlich.dsl.parser.listener.BnfListener;
import de.etgramlich.dsl.generator.InterfaceBuilder;
import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Main program that converts a EBNF grammar to Java interfaces.
 */
public final class Main {
    private Main() { }

    /**
     * A regex pattern matching a non-terminal of the grammar.
     */
    private static final Pattern NON_TERMINAL_PATTERN = Pattern.compile("<[a-zA-Z]+>");

    /**
     * Help option to be used in own method.
     */
    private static final Option HELP_OPTION =
            Option.builder("h").required(false).hasArg(false).longOpt("help").desc("Prints this help text.").build();

    /**
     * Stores command line options.
     */
    private static final Options OPTIONS = new Options();
    static {
        OPTIONS.addOption(HELP_OPTION);
        OPTIONS.addRequiredOption("d", "directory", true, "Target directory for generated sources");
        OPTIONS.addRequiredOption("p", "package", true, "Target package");
        OPTIONS.addRequiredOption("g", "grammar", true, "Grammar file path");
        OPTIONS.addOption("s", "sketch-graph", true, "Writes DOT graph to file");
    }

    /**
     * Save line separator for current system.
     */
    private static final String NEWLINE = System.lineSeparator();

    /**
     * Syntax hint for help formatter.
     */
    private static final String CMD_SYNTAX = "java -jar antlr-test-x.x-jar-with-dependencies.jar";

    /**
     * Header for help test.
     */
    private static final String HEADER = "Generate interfaces from EBNF grammar" + NEWLINE + NEWLINE;

    /**
     * Footer for help text.
     */
    private static final String FOOTER = NEWLINE + "Report issues at https://github.com/etgramli/AntlrTest/";

    /**
     * Reads grammar from file and outputs java interfaces in the corresponding package directory in the provided
     * target directory.
     * @param args Command line arguments to be parsed. Should not be empty.
     */
    public static void main(final String[] args) {
        final String grammar;
        final String targetDirectory;
        final String targetPackage;
        final String graphFilePath;
        try {
            if (testForHelp(args)) {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(CMD_SYNTAX, HEADER, OPTIONS, FOOTER, true);
                return;
            }
            final CommandLine cmd = new DefaultParser().parse(OPTIONS, args);
            targetDirectory = cmd.getOptionValue("d");
            targetPackage = cmd.getOptionValue("p");
            grammar = prepareGrammar(cmd.getOptionValue("g"));
            graphFilePath = cmd.hasOption("s") ? cmd.getOptionValue("s") : null;
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.println("Could not parse CMD arguments! Run with -h for help.");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Grammar file could not be read!");
            return;
        }

        final BnfListener listener = new BnfListener();
        listener.enterBnf(new BnfParser(new CommonTokenStream(new BnfLexer(CharStreams.fromString(grammar)))).bnf());
        final BnfRuleGraph graph = new ForestBuilder(listener.getBnf()).getMergedGraph();

        try {
            if (graphFilePath != null) {
                graph.renderBnfRuleGraph(targetDirectory + File.separator + graphFilePath + ".gv");
            }
            new InterfaceBuilder(targetDirectory, targetPackage).saveInterfaces(graph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean testForHelp(final String[] args) throws ParseException {
        final Options options = new Options();
        options.addOption(HELP_OPTION);
        final CommandLine commandLine = new DefaultParser().parse(options, args, true);
        return commandLine.hasOption(HELP_OPTION.getOpt());
    }

    private static String prepareGrammar(final String filepath) throws IOException {
        return Files.readAllLines(Paths.get(filepath)).stream()
                .dropWhile(line -> !NON_TERMINAL_PATTERN.matcher(line).matches())
                .filter(line -> !StringUtils.isBlank(line))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
