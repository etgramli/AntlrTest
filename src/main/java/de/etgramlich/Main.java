package de.etgramlich;

import de.etgramlich.graph.ForestBuilder;
import de.etgramlich.parser.gen.bnf.BnfLexer;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.listener.BnfListener;
import de.etgramlich.parser.type.Bnf;
import de.etgramlich.util.StringUtil;
import de.etgramlich.graph.GraphBuilder;
import de.etgramlich.generator.InterfaceBuilder;
import de.etgramlich.graph.type.BnfRuleGraph;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Main program that converts a EBNF grammar to Java interfaces.
 */
public final class Main {
    private Main() { }

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

        final BnfRuleGraph mergedGraph = new ForestBuilder(bnf).getMergedGraph();
        final BnfRuleGraph graph = new GraphBuilder(bnf).getGraph();
        try {
            graph.renderBnfRuleGraph(targetDirectory + File.separator + "graph.gv");
            mergedGraph.renderBnfRuleGraph(targetDirectory + File.separator + "graph_merged.gv");

            new InterfaceBuilder(targetDirectory, targetPackage).saveInterfaces(mergedGraph);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String prepareGrammar(final String filepath) throws IOException {
        final List<String> grammar = Files.readAllLines(Paths.get(filepath));
        final int beginIndex = grammar.indexOf(
                grammar.stream().filter(i -> i.matches("grammar .*;")).findFirst().orElse(null)
        );
        final List<String> allRules = grammar.subList(beginIndex + 1, grammar.size());
        final List<String> noDuplicateRules = StringUtil.removeDuplicates(StringUtil.stripBlankLines(allRules));
        return String.join(System.lineSeparator(), noDuplicateRules);
    }
}
