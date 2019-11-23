package de.etgramlich.antlr;

import de.etgramlich.antlr.parser.gen.bnf.bnfLexer;
import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import de.etgramlich.antlr.parser.listener.RuleListListener;
import de.etgramlich.antlr.parser.type.RuleList;
import de.etgramlich.antlr.util.StringUtil;
import de.etgramlich.antlr.util.graph.GraphBuilder;
import de.etgramlich.antlr.util.graph.Scope;
import de.etgramlich.antlr.util.graph.ScopeEdge;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.GraphExporter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.jgrapht.io.ExportException;
import java.util.List;

public final class Main {
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
            System.err.println("Wrong grammar file!");
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
        ruleList.removeNonTerminals();

        GraphBuilder gb = new GraphBuilder(ruleList);
        Graph<Scope, ScopeEdge> graph = gb.getGraph();
        try {
            renderHrefGraph(graph);
        } catch (ExportException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private static String prepareGrammar(final String filepath) throws IOException {
        final List<String> grammar = Files.readAllLines(Paths.get(filepath));
        final int beginIndex = grammar.indexOf(
                grammar.stream().filter(i -> i.matches("grammar .*;")).findFirst().orElse(null)
        );
        final List<String> allRules = grammar.subList(beginIndex + 1, grammar.size());
        final List<String> noDuplicateRules = StringUtil.removeDuplicates(StringUtil.stripBlankLines(allRules));
        return String.join("\n", noDuplicateRules);
    }

    private static void renderHrefGraph(Graph<Scope, ScopeEdge> hrefGraph) throws ExportException {
        final ComponentNameProvider<Scope> vertexProvider = scope -> "S_" + scope.getName();
        final ComponentNameProvider<Scope> vertexIdProvider = scope -> "S_id_" + scope.getName();
        final ComponentNameProvider<ScopeEdge> edgeProvider =
                scopeEdge -> "E_" + scopeEdge.getNodes().get(0).getName() + "(" + scopeEdge.getNodes().size() + ")";
        final GraphExporter<Scope, ScopeEdge> exporter =
                new DOTExporter<>(vertexIdProvider, vertexProvider, edgeProvider);
        final Writer writer = new StringWriter();
        exporter.exportGraph(hrefGraph, writer);
        System.out.println(writer.toString());
    }
}
