package de.etgramlich.graph;

import de.etgramlich.parser.type.Bnf;
import de.etgramlich.parser.type.BnfRule;
import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.Sequence;
import de.etgramlich.parser.type.Element;
import de.etgramlich.parser.type.repetition.AbstractRepetition;
import de.etgramlich.parser.type.repetition.Optional;
import de.etgramlich.parser.type.repetition.ZeroOrMore;
import de.etgramlich.parser.type.text.TextElement;
import de.etgramlich.graph.type.BnfRuleGraph;
import de.etgramlich.graph.type.Scope;
import de.etgramlich.graph.type.ScopeEdge;
import de.etgramlich.graph.type.NodeEdge;
import de.etgramlich.graph.type.OptionalEdge;
import de.etgramlich.graph.type.RepetitionEdge;
import de.etgramlich.graph.type.Node;
import de.etgramlich.graph.type.NodeType;
import de.etgramlich.util.exception.InvalidGraphException;
import de.etgramlich.util.exception.UnrecognizedElementException;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Builds a Graph with Scopes as vertices and Node as edges.
 * List of rules must be passed as constructor argument, getGraph can be passed on each constructed object.
 */
public final class GraphBuilder {
    /**
     * Graph representing the bnf passed in constructor.
     * Can be accessed if constructor succeeds.
     */
    private final BnfRuleGraph graph;

    /**
     * Saves the last added scope.
     */
    private Scope lastAddedScope;

    /**
     * Creates a BnfRuleGraph from a Bnf (tree).
     * @param bnf A bnf tree, must not be null.
     */
    public GraphBuilder(final Bnf bnf) {
        final List<BnfRule> startBnfRules = bnf.getBnfRules().stream()
                .filter(BnfRule::isStartRule)
                .collect(Collectors.toList());
        if (startBnfRules.size() != 1) {
            throw new IllegalArgumentException("Rule-list must have exactly one Start rule");
        }
        final BnfRule startBnfRule = startBnfRules.get(0);
        final List<BnfRule> nonTerminalBnfRules = bnf.getBnfRules().stream()
                .filter(bnfRule -> !bnfRule.isTerminal() && bnfRule.getNumberOfElements() > 1)
                .collect(Collectors.toList());
        nonTerminalBnfRules.remove(startBnfRule);

        // Add first scope
        graph = new BnfRuleGraph(startBnfRule.getName());
        lastAddedScope = graph.addVertex();
        // Parse all rules
        for (BnfRule bnfRule : nonTerminalBnfRules) {
            processAlternatives(bnfRule.getRhs());
        }

        if (!graph.isConsistent()) {
            throw new InvalidGraphException("Graph is not consistent after build!");
        }
    }

    /**
     * Creates a graph for the provided rule.
     * @param rule BnfRule, must not be null.
     */
    public GraphBuilder(final BnfRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("BnfRule must not be null!");
        }
        graph = new BnfRuleGraph(rule.getName());
        lastAddedScope = graph.addVertex();

        processAlternatives(rule.getRhs());

        if (!graph.isConsistent()) {
            throw new InvalidGraphException("Graph is not consistent after build!");
        }
    }

    private void processAlternatives(final Alternatives alternatives) {
        assert (!alternatives.getSequences().isEmpty());

        final Scope openingAlternativeScope = lastAddedScope;
        final Set<Scope> lastScopes = new HashSet<>();

        for (Iterator<Sequence> iter = alternatives.getSequences().iterator(); iter.hasNext();) {
            processSequence(iter.next());
            lastScopes.add(lastAddedScope);
            if (iter.hasNext()) {
                lastAddedScope = openingAlternativeScope;
            }
        }

        // Replace scopes of dangling edges with only one (new) one to implement recursive alternatives
        if (lastScopes.size() > 1) {
            final Scope closingAlternativeScope = graph.addVertex();
            mergeNodes(closingAlternativeScope, lastScopes);
            lastAddedScope = closingAlternativeScope;
        }
    }

    /**
     * Replaces the scopes by the new scope, to merge edges to a single target or source vertex.
     * @param newScope New scope, must not be null.
     * @param scopes Collection with scopes to replace.
     */
    private void mergeNodes(final Scope newScope, final Collection<Scope> scopes) {
        if (newScope == null || scopes == null) {
            throw new IllegalArgumentException("New scope and scopes must not be null!");
        }
        final Set<ScopeEdge> ingoingEdges = new HashSet<>();
        final Set<ScopeEdge> outgoingEdges = new HashSet<>();
        for (Scope scope : scopes) {
            ingoingEdges.addAll(graph.incomingEdgesOf(scope));
            outgoingEdges.addAll(graph.outgoingEdgesOf(scope));
        }
        // Remove vertices and touching edges
        graph.removeAllVertices(scopes);

        // Remove old target vertex and edge, re-add edge with updated vertex
        for (ScopeEdge edge : ingoingEdges) {
            graph.addEdge(edge.getSource(), newScope, edge);
        }

        // Remove old source vertex and edge, re-add edge with updated vertex
        for (ScopeEdge edge : outgoingEdges) {
            graph.addEdge(newScope, edge.getTarget(), edge);
        }
    }

    private void processSequence(final Sequence sequence) {
        assert (sequence.getElements().size() > 0);

        for (Element element : sequence.getElements()) {
            processElement(element);
        }
    }

    /**
     * Processes given element and adds it to the Graph.
     *
     * @param element Element of EBNF grammar to be added, must not be null.
     */
    private void processElement(final Element element) {
        if (element instanceof TextElement) {
            final TextElement textElement = (TextElement) element;

            addNodeInSequence(new Node(textElement.getName(), NodeType.fromTextElement(textElement)));
        } else if (element instanceof AbstractRepetition) {
            AbstractRepetition repetition = (AbstractRepetition) element;

            final Scope beforeOptionalLoop = lastAddedScope;
            processAlternatives(repetition.getAlternatives());

            if (repetition instanceof Optional || repetition instanceof ZeroOrMore) {
                graph.addEdge(beforeOptionalLoop, lastAddedScope, new OptionalEdge());
                if (repetition instanceof ZeroOrMore) {
                    graph.addEdge(lastAddedScope, beforeOptionalLoop, new RepetitionEdge());
                }
            }
        } else {
            throw new UnrecognizedElementException("Element not recognized: " + element.toString());
        }
    }

    /**
     * Replaces non-terminal nodes with graph from the forest.
     * Assumes that all required non-terminals exist in the branch.
     * @param forest Set of BnfRuleGraphs, must not be null. Arguments will be altered by this call.
     */
    public void replaceNonTerminals(final Set<BnfRuleGraph> forest) {
        final Set<String> nonTerminals = graph.getNonTerminalNodes().stream()
                .map(Node::getName).collect(Collectors.toUnmodifiableSet());
        final Set<String> forestRuleNames = forest.stream()
                .map(BnfRuleGraph::getName).collect(Collectors.toUnmodifiableSet());
        if (!forestRuleNames.containsAll(nonTerminals)) {
            throw new IllegalArgumentException("Forest does not contain all required non-terminals!");
        }

        for (NodeEdge edge : graph.getNonTerminalNodeEdges()) {
            if (!forestRuleNames.contains(edge.getNode().getName())) {
                throw new IllegalArgumentException("Missing rule in forest: " + edge.getNode().getName());
            }
            // Replace NodeEdge by graph
            final BnfRuleGraph graph = forest.stream()
                    .filter(bnfRuleGraph -> edge.getNode().getName().equals(bnfRuleGraph.getName()))
                    .findFirst().orElse(null);
            if (graph == null) {
                throw new IllegalArgumentException("Missing rule in forest: " + edge.getNode().getName());
            }
            replaceNonTerminalNodeEdge(edge, graph);
        }
    }

    private void replaceNonTerminalNodeEdge(final NodeEdge nodeEdge, final BnfRuleGraph nodeEdgeReplacement) {
        final Map<Scope, Scope> originalScopeToReplacedScope = new HashMap<>(nodeEdgeReplacement.vertexSet().size());
        for (Scope scope : nodeEdgeReplacement.vertexSet()) {
            originalScopeToReplacedScope.put(scope, graph.addVertex());
        }
        for (ScopeEdge edge : nodeEdgeReplacement.edgeSet()) {
            graph.addEdge(
                    originalScopeToReplacedScope.get(edge.getSource()),
                    originalScopeToReplacedScope.get(edge.getTarget()),
                    (ScopeEdge) edge.clone());
        }

        final Scope subGraphStart = originalScopeToReplacedScope.get(nodeEdgeReplacement.getStartScope());
        mergeNodes(subGraphStart, Collections.singleton(nodeEdge.getSource()));
        final Scope subGraphEnd = originalScopeToReplacedScope.get(nodeEdgeReplacement.getEndScope());
        mergeNodes(subGraphEnd, Collections.singleton(nodeEdge.getTarget()));

        graph.removeEdge(nodeEdge);

        if (!graph.isConsistent()) {
            System.err.println(graph);
            throw new InvalidGraphException("Error replacing non-terminal: " + nodeEdge.getNode().getName());
        }
    }

    /**
     * Returns a copy of the graph.
     * @return BnfRuleGraph, not null.
     */
    public BnfRuleGraph getGraph() {
        return (BnfRuleGraph) graph.clone();
    }

    /**
     * Adds a node in sequence to the graph following the last added scope and followed by a newly created scope.
     *
     * @param node  Node(s) to be associated with the edge.
     */
    public void addNodeInSequence(final Node node) {
        final Scope newScope = graph.addVertex();
        graph.addEdge(lastAddedScope, newScope, new NodeEdge(node));
        lastAddedScope = newScope;
    }
}
