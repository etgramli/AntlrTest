package de.etgramlich.dsl.graph;

import de.etgramlich.dsl.parser.type.Bnf;
import de.etgramlich.dsl.parser.type.BnfRule;
import de.etgramlich.dsl.parser.type.Alternatives;
import de.etgramlich.dsl.parser.type.Sequence;
import de.etgramlich.dsl.parser.type.Element;
import de.etgramlich.dsl.parser.type.repetition.AbstractRepetition;
import de.etgramlich.dsl.parser.type.repetition.Optional;
import de.etgramlich.dsl.parser.type.repetition.ZeroOrMore;
import de.etgramlich.dsl.parser.type.text.TextElement;
import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import de.etgramlich.dsl.graph.type.Scope;
import de.etgramlich.dsl.graph.type.ScopeEdge;
import de.etgramlich.dsl.graph.type.NodeEdge;
import de.etgramlich.dsl.graph.type.OptionalEdge;
import de.etgramlich.dsl.graph.type.Node;
import de.etgramlich.dsl.graph.type.NodeType;
import de.etgramlich.dsl.util.CollectionUtil;
import de.etgramlich.dsl.util.exception.InvalidGraphException;
import de.etgramlich.dsl.util.exception.UnrecognizedElementException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builds a Graph with Scopes as vertices and Node as edges.
 * List of rules must be passed as constructor argument, getGraph can be passed on each constructed object.
 */
public final class GraphBuilder {
    /**
     * Graph representing the bnf passed to the constructor.
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

        graph = new BnfRuleGraph(startBnfRule.getName());
        lastAddedScope = graph.addVertex();
        graph.setStartScope(lastAddedScope);
        graph.setEndScope(lastAddedScope);

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
        graph.setStartScope(lastAddedScope);

        processAlternatives(rule.getRhs());

        if (!graph.isConsistent()) {
            throw new InvalidGraphException("Graph is not consistent after build!");
        }
    }

    private Set<NodeEdge> processAlternatives(final Alternatives alternatives) {
        assert (!alternatives.getSequences().isEmpty());

        final Scope openingAlternativeScope = lastAddedScope;
        final Set<Scope> lastScopes = new HashSet<>(alternatives.getSequences().size());
        final Set<NodeEdge> lastEdges = new HashSet<>(alternatives.getSequences().size());

        for (Iterator<Sequence> iter = alternatives.getSequences().iterator(); iter.hasNext();) {
            lastEdges.addAll(processSequence(iter.next()));
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
        graph.setEndScope(lastAddedScope);
        return Collections.unmodifiableSet(lastEdges);
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
        if (!graph.vertexSet().contains(newScope)) {
            throw new IllegalArgumentException("Graph does not contain the scope " + newScope);
        }
        if (!graph.vertexSet().containsAll(scopes)) {
            final Set<String> missing = scopes.stream()
                    .filter(s -> !graph.vertexSet().contains(s)).map(Scope::getName)
                    .collect(Collectors.toUnmodifiableSet());
            throw new IllegalArgumentException("Graph must contain all scopes to be replaced! (missing: "
                    + CollectionUtil.asString(missing));
        }
        final Set<ScopeEdge> ingoingEdges = new HashSet<>();
        final Set<ScopeEdge> outgoingEdges = new HashSet<>();
        for (Scope scope : scopes) {
            ingoingEdges.addAll(graph.incomingEdgesOf(scope));
            outgoingEdges.addAll(graph.outgoingEdgesOf(scope));
        }
        final Set<ScopeEdge> selfEdges = Stream.concat(ingoingEdges.stream(), outgoingEdges.stream())
                .filter(edge -> scopes.contains(edge.getSource()) && scopes.contains(edge.getTarget()))
                .collect(Collectors.toSet());
        ingoingEdges.removeAll(selfEdges);
        outgoingEdges.removeAll(selfEdges);

        graph.removeAllVertices(scopes);
        ingoingEdges.forEach(e -> graph.addEdge(e.getSource(), newScope, e));
        outgoingEdges.forEach(e -> graph.addEdge(newScope, e.getTarget(), e));
        selfEdges.forEach(e -> graph.addEdge(newScope, newScope, e));

        if (scopes.contains(graph.getStartScope())) {
            graph.setStartScope(newScope);
        }
        if (scopes.contains(graph.getEndScope())) {
            graph.setEndScope(newScope);
        }
    }

    private Set<NodeEdge> processSequence(final Sequence sequence) {
        if (sequence == null) {
            throw new IllegalArgumentException("Sequence must not be null!");
        }
        if (sequence.getElements().isEmpty()) {
            throw new IllegalArgumentException("Sequence must have at least one element!");
        }
        Set<NodeEdge> lastEdges = Collections.emptySet();
        for (Element element : sequence.getElements()) {
            lastEdges = processElement(element);
        }
        return Collections.unmodifiableSet(lastEdges);
    }

    /**
     * Processes given element and adds it to the Graph.
     *
     * @param element Element of EBNF grammar to be added, must not be null.
     * @return Set of the last returned edges (before the lastAddedScope);
     */
    private Set<NodeEdge> processElement(final Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null!");
        }
        if (element instanceof TextElement) {
            return processTextElement((TextElement) element);
        } else if (element instanceof AbstractRepetition) {
            return processAbstractRepetition((AbstractRepetition) element);
        } else {
            throw new UnrecognizedElementException("Element not recognized: " + element.toString());
        }
    }

    /**
     * Adds a new node in sequence to the last added scope with the name of the provided text element.
     * @param textElement TextElement, must not be null.
     * @return The edge before the newly added scope.
     */
    private Set<NodeEdge> processTextElement(final TextElement textElement) {
        if (textElement == null) {
            throw new IllegalArgumentException("TextElement must not be null!");
        }
        final Scope newScope = graph.addVertex();

        final NodeEdge nodeEdge = new NodeEdge(new Node(textElement.getName(), NodeType.fromTextElement(textElement)));
        graph.addEdge(lastAddedScope, newScope, nodeEdge);

        lastAddedScope = newScope;
        graph.setEndScope(lastAddedScope);
        return Set.of(nodeEdge);
    }

    private Set<NodeEdge> processAbstractRepetition(final AbstractRepetition repetition) {
        final Scope beforeOptionalLoop = lastAddedScope;

        final Set<NodeEdge> lastAddedEdges = processAlternatives(repetition.getAlternatives());

        if (repetition instanceof Optional || repetition instanceof ZeroOrMore) {
            graph.addEdge(beforeOptionalLoop, lastAddedScope, new OptionalEdge());
            if (repetition instanceof ZeroOrMore) {
                for (NodeEdge edgeToDuplicate : lastAddedEdges) {
                    for (Scope entryPointOfLoop : getSecondScopeOfPath(beforeOptionalLoop, lastAddedScope)) {
                        graph.addEdge(lastAddedScope, entryPointOfLoop, new NodeEdge(edgeToDuplicate.getNode()));
                    }
                }
            }
        }
        return lastAddedEdges;
    }

    private Set<Scope> getSecondScopeOfPath(final Scope start, final Scope end) {
        final AllDirectedPaths<Scope, ScopeEdge> allPaths = new AllDirectedPaths<>(graph.copyWithoutBackwardEdges());
        final List<GraphPath<Scope, ScopeEdge>> paths = allPaths.getAllPaths(start, end, true, null);
        if (paths.isEmpty()) {
            throw new InvalidGraphException("There must be at least one path between " + start + " and " + end);
        }
        final Set<ScopeEdge> firstEdges = paths.stream()
                .map(path -> path.getEdgeList().get(0))
                .collect(Collectors.toUnmodifiableSet());
        if (!firstEdges.stream().allMatch(edge -> edge instanceof NodeEdge)) {
            throw new InvalidGraphException("First edge of all paths must be a NodeEdge!");
        }
        return firstEdges.stream().map(ScopeEdge::getTarget).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Replaces non-terminal nodes with a graph from the forest.
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

        while (graph.containsNonTerminals()) {
            final NodeEdge nonTerminalEdge = graph.getNonTerminalNodeEdges().iterator().next();
            if (!forestRuleNames.contains(nonTerminalEdge.getNode().getName())) {
                throw new IllegalArgumentException("Missing rule in forest: " + nonTerminalEdge.getNode().getName());
            }
            final java.util.Optional<BnfRuleGraph> edgeReplacement = forest.stream()
                    .filter(bnfRuleGraph -> nonTerminalEdge.getNode().getName().equals(bnfRuleGraph.getName()))
                    .findFirst();
            if (edgeReplacement.isEmpty()) {
                throw new IllegalArgumentException("Missing rule in forest: " + nonTerminalEdge.getNode().getName());
            }
            replaceNonTerminalNodeEdge(nonTerminalEdge, edgeReplacement.get());
        }
    }

    private void replaceNonTerminalNodeEdge(final NodeEdge nodeEdge, final BnfRuleGraph nodeEdgeReplacement) {
        final Map<Scope, Scope> scopeMap = new HashMap<>(nodeEdgeReplacement.vertexSet().size());
        nodeEdgeReplacement.vertexSet().forEach(scope -> scopeMap.put(scope, graph.addVertex()));

        for (ScopeEdge edge : nodeEdgeReplacement.edgeSet()) {
            graph.addEdge(scopeMap.get(edge.getSource()), scopeMap.get(edge.getTarget()), (ScopeEdge) edge.clone());
        }

        if (nodeEdge.getSource() != nodeEdge.getTarget()) {
            mergeNodes(scopeMap.get(nodeEdgeReplacement.getStartScope()), Set.of(nodeEdge.getSource()));
            mergeNodes(scopeMap.get(nodeEdgeReplacement.getEndScope()), Set.of(nodeEdge.getTarget()));
        } else {
            mergeNodes(nodeEdge.getTarget(), Set.of(scopeMap.get(nodeEdgeReplacement.getStartScope())));
            mergeNodes(nodeEdge.getTarget(), Set.of(scopeMap.get(nodeEdgeReplacement.getEndScope())));
        }

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
}
