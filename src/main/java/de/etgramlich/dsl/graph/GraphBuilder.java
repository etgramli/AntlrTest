package de.etgramlich.dsl.graph;

import com.google.common.collect.Sets;
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
import de.etgramlich.dsl.util.exception.InvalidGraphException;
import de.etgramlich.dsl.util.exception.UnrecognizedElementException;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

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

    private void mergeNodes(final Scope newScope, final Scope toReplace) {
        mergeNodes(newScope, Set.of(toReplace));
    }

    /**
     * Replaces the scopes by the new scope, to merge edges to a single target or source vertex.
     * @param newScope New scope, must not be null.
     * @param scopes Collection with scopes to replace.
     */
    private void mergeNodes(final Scope newScope, final Set<Scope> scopes) {
        if (newScope == null || scopes == null) {
            throw new IllegalArgumentException("New scope and scopes must not be null!");
        }
        if (!graph.vertexSet().contains(newScope)) {
            throw new IllegalArgumentException("Graph does not contain the scope " + newScope);
        }
        if (!graph.vertexSet().containsAll(scopes)) {
            throw new IllegalArgumentException("Graph must contain all scopes to be replaced! (missing: "
                    + Sets.difference(scopes, graph.vertexSet()).stream().map(Scope::getName)
                    .collect(joining(", ")));
        }
        final Set<ScopeEdge> ingoingEdges = new HashSet<>();
        final Set<ScopeEdge> outgoingEdges = new HashSet<>();
        for (Scope scope : scopes) {
            ingoingEdges.addAll(graph.incomingEdgesOf(scope));
            outgoingEdges.addAll(graph.outgoingEdgesOf(scope));
        }
        final Set<ScopeEdge> selfEdges = Stream.concat(ingoingEdges.stream(), outgoingEdges.stream())
                .filter(edge -> scopes.contains(edge.getSource()) && scopes.contains(edge.getTarget()))
                .collect(toSet());
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

        if (repetition instanceof Optional) {
            graph.addEdge(beforeOptionalLoop, lastAddedScope, new OptionalEdge());
        }
        if (repetition instanceof ZeroOrMore) {
            final Set<ScopeEdge> firstEdges = getFirstEdgesOfPaths(beforeOptionalLoop, lastAddedScope);
            final Set<ScopeEdge> lastEdges = getLastEdgesOfPaths(beforeOptionalLoop, lastAddedScope);

            final Scope loopScope = graph.addVertex();
            changeSourceScope(loopScope, firstEdges);
            changeTargetScope(loopScope, lastEdges);

            graph.addEdge(beforeOptionalLoop, loopScope, new OptionalEdge());
            graph.addEdge(loopScope, lastAddedScope, new OptionalEdge());
        }
        return lastAddedEdges;
    }

    private void changeSourceScope(final Scope newSource, final Collection<ScopeEdge> edges) {
        for (ScopeEdge edge : edges) {
            graph.removeEdge(edge);
            graph.addEdge(newSource, edge.getTarget(), edge);
        }
    }

    private void changeTargetScope(final Scope newTarget, final Collection<ScopeEdge> edges) {
        for (ScopeEdge edge : edges) {
            graph.removeEdge(edge);
            graph.addEdge(edge.getSource(), newTarget, edge);
        }
    }

    private Set<ScopeEdge> getLastEdgesOfPaths(final Scope start, final Scope end) {
        final Set<ScopeEdge> lastEdges =
                new AllDirectedPaths<>(graph.copyWithoutBackwardEdges()).getAllPaths(start, end, true, null).stream()
                        .map(path -> path.getEdgeList().get(path.getEdgeList().size() - 1))
                        .collect(toUnmodifiableSet());
        if (lastEdges.isEmpty()) {
            throw new IllegalArgumentException("There must be at least one path between " + start + " and " + end);
        }
        return lastEdges;
    }

    private Set<ScopeEdge> getFirstEdgesOfPaths(final Scope start, final Scope end) {
        final Set<ScopeEdge> firstEdges =
                new AllDirectedPaths<>(graph.copyWithoutBackwardEdges()).getAllPaths(start, end, true, null).stream()
                .map(path -> path.getEdgeList().get(0))
                .collect(toUnmodifiableSet());
        if (firstEdges.isEmpty()) {
            throw new InvalidGraphException("There must be at least one path between " + start + " and " + end);
        }
        return firstEdges;
    }

    /**
     * Replaces non-terminal nodes with a graph from the forest.
     * Assumes that all required non-terminals exist in the branch.
     * @param forest Set of BnfRuleGraphs, must not be null. Arguments will be altered by this call.
     */
    public void replaceNonTerminals(final Set<BnfRuleGraph> forest) {
        final Set<String> nonTerminals = graph.getNonTerminalNodes().stream()
                .map(Node::getName)
                .collect(toUnmodifiableSet());
        final Set<String> forestRuleNames = forest.stream()
                .map(BnfRuleGraph::getName)
                .collect(toUnmodifiableSet());
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

        mergeNodes(nodeEdge.getSource(), scopeMap.get(nodeEdgeReplacement.getStartScope()));
        mergeNodes(nodeEdge.getTarget(), scopeMap.get(nodeEdgeReplacement.getEndScope()));

        graph.removeEdge(nodeEdge);

        if (!graph.isConsistent()) {
            System.err.println(graph);
            throw new InvalidGraphException("Error replacing non-terminal: " + nodeEdge.getNode().getName());
        }
    }

    /**
     * Removed scopes with only one ingoing OptionalEdge and only one outgoing OptionalEdge and thus would only produce
     * an intermediate interface with no methods.
     */
    public void removeSuperfluousScopes() {
        final Set<Scope> toRemove = graph.vertexSet().stream()
                .filter(scope -> graph.inDegreeOf(scope) == 1 && graph.outDegreeOf(scope) == 1)
                .filter(scope -> graph.incomingEdgesOf(scope).iterator().next() instanceof OptionalEdge)
                .filter(scope -> graph.outgoingEdgesOf(scope).iterator().next() instanceof OptionalEdge)
                .collect(toUnmodifiableSet());
        for (Scope scope : toRemove) {
            Scope source = graph.incomingEdgesOf(scope).iterator().next().getSource();
            Scope target = graph.outgoingEdgesOf(scope).iterator().next().getTarget();
            graph.removeVertex(scope);
            graph.addEdge(source, target, new OptionalEdge());
        }

        if (!graph.isConsistent()) {
            throw new InvalidGraphException("Graph not consistent after scope removal!");
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
