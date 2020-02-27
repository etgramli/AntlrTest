package de.etgramlich;

import de.etgramlich.parser.gen.bnf.BnfLexer;
import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.listener.BnfListener;
import de.etgramlich.parser.type.Bnf;
import de.etgramlich.util.StringUtil;
import de.etgramlich.graph.GraphBuilder;
import de.etgramlich.generator.InterfaceBuilder;
import de.etgramlich.graph.type.BnfRuleGraph;
import de.etgramlich.graph.type.NodeEdge;
import de.etgramlich.graph.type.Scope;
import de.etgramlich.graph.type.ScopeEdge;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.Graph;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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
        OPTIONS.addOption("d", "directory", true, "Target directory for generated sources");
        OPTIONS.addOption("p", "package", true, "Target package");
        OPTIONS.addOption("g", "grammar", true, "Grammar file path");
    }

    /**
     * Reads grammar from file and outputs java interfaces in the corresponding package directory in the provided
     * target directory.
     * @param args Command line arguments to be parsed. Should not be empty.
     */
    public static void main(final String[] args) {
        final String grammar;
        String targetDirectory = "." + File.separator;
        String targetPackage = StringUtils.EMPTY;
        try {
            CommandLine cmd = new DefaultParser().parse(OPTIONS, args);
            if (cmd.hasOption("d")) {
                targetDirectory = cmd.getOptionValue("d");
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
            System.err.println("Wrong grammar file!");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Grammar file could not be read!");
            return;
        }

        final BnfParser parser = new BnfParser(new CommonTokenStream(new BnfLexer(CharStreams.fromString(grammar))));

        // Parse given Grammar and get tree of types
        final BnfListener listener = new BnfListener();
        listener.enterBnf(parser.bnf());
        final Bnf bnf = listener.getBnf();

        final BnfRuleGraph graph = new GraphBuilder(bnf).getGraph();

        try {
            renderBnfRuleGraph(graph, targetDirectory + File.separator + "graph.gv");

            InterfaceBuilder builder = new InterfaceBuilder(targetDirectory, targetPackage);
            builder.saveInterfaces(graph);
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

    private static void renderBnfRuleGraph(final Graph<Scope, ScopeEdge> bnfRuleGraph, final String path)
            throws IOException {
        final DOTExporter<Scope, ScopeEdge> exporter = new DOTExporter<>(Scope::getName);
        exporter.setEdgeIdProvider(
                scopeEdge -> "E_" + (scopeEdge instanceof NodeEdge ? ((NodeEdge) scopeEdge).getNode().getName()
                                                                    : scopeEdge.getClass().getName()));
        exporter.setEdgeAttributeProvider(
                edge -> Map.of("name", new DefaultAttribute<>(
                        (edge instanceof NodeEdge ? ((NodeEdge) edge).getNode().getName()
                                                  : edge.getClass().getName()), AttributeType.STRING)));

        try (FileWriter fileWriter = new FileWriter(path, StandardCharsets.UTF_8)) {
            exporter.exportGraph(bnfRuleGraph, fileWriter);
        }
    }
}
