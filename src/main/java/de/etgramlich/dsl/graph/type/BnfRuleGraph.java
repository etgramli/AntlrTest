package de.etgramlich.dsl.graph.type;

import de.etgramlich.dsl.util.exception.InvalidGraphException;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Builds a graph according to the rules of a EBNF (has to care about optional elements and repetitions).
 */
public final class BnfRuleGraph extends DirectedPseudograph<Scope, ScopeEdge> {
    private static final long serialVersionUID = 8785829155769370692L;

    /**
     * Name to identify graph by (LHS) bnf rule name.
     */
    private final String name;

    /**
     * Creates an unweighted BnfRuleGraph with a vertex supplier and without an edge supplier.
     *
     * @param name Name of the graph (used for BNF rule LHS).
     */
    public BnfRuleGraph(final String name) {
        super(new ScopeSupplier(), null, false);
        this.name = name;
    }

    /**
     * Returns the length from the start node to end node.
     *
     * @return Non-negative integer.
     */
    public int length() {
        if (isEmpty()) {
            return 0;
        } else {
            return DijkstraShortestPath.findPathBetween(this, getStartScope(), getEndScope()).getLength();
        }
    }

    /**
     * Returns the (LSH side) name of the graph.
     *
     * @return String, not null.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns true if the graph contains no edges and no vertices.
     *
     * @return True if no edges and vertices in graph.
     */
    public boolean isEmpty() {
        return edgeSet().isEmpty() && vertexSet().isEmpty();
    }

    /**
     * Tests whether the graph is consistent. It must:
     * - have one start vertex,
     * - have one end vertex,
     * - have a parallel connection to any optional edge
     * - have a parallel connection to any repetition edge (in reverse direction)
     *
     * @return True if the graph is consistent or empty.
     */
    public boolean isConsistent() {
        if (edgeSet().isEmpty() && vertexSet().isEmpty()) {
            return true;
        }
        for (ScopeEdge edge : edgeSet()) {
            if (!vertexSet().contains(edge.getSource()) || !vertexSet().contains(edge.getTarget())) {
                return false;
            } else if (edge instanceof OptionalEdge && notConnectedByNodeEdges(edge.getSource(), edge.getTarget())) {
                return false;
            }
        }
        try {
            getStartScope();
            getEndScope();
        } catch (InvalidGraphException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns all scopes that are connected by any type of outgoing edge.
     *
     * @param scope Scope, must not be null and present in graph.
     * @return Set of scopes, not null, may be empty.
     */
    public Set<Scope> getOutGoingScopes(final Scope scope) {
        return outgoingEdgesOf(scope).stream()
                .filter(edge -> edge.getSource() != edge.getTarget())
                .map(ScopeEdge::getTarget)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the successors of a node in the directed graph.
     * Only scopes connected by a NodeEdge are considered successors.
     *
     * @param scope Scope to find its successors.
     * @return List of Scopes.
     */
    public Set<Scope> getSuccessors(final Scope scope) {
        return outGoingNodeEdges(scope).stream()
                .map(ScopeEdge::getTarget)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Get the closest preceding non-terminals, connected by NodeEdges. Also, non-terminals that are indirectly
     * connected (by a terminal in a NodeEdge) are found.
     *
     * @param scope Scope, must not be null and present in the graph.
     * @return Set of Scopes, not null, may be empty.
     */
    public Set<Scope> getPrecedingKeywords(final Scope scope) {
        final Set<Scope> predecessors = new HashSet<>(getPredecessors(scope));
        final Set<Scope> keywords = predecessors.stream()
                .filter(s -> ((NodeEdge) getEdge(s, scope)).getNode().getType().equals(NodeType.KEYWORD))
                .collect(Collectors.toSet());
        predecessors.removeAll(keywords);   // Now here are all non types
        predecessors.forEach(p -> keywords.addAll(getPrecedingKeywords(p)));
        return Collections.unmodifiableSet(keywords);
    }

    /**
     * Get the closest following scopes following a NodeEdge containing a TYPE node.
     * @param scope Scope, must not be null, must be in the graph.
     * @return Set of Scopes, not null, may be empty.
     */
    public Set<Scope> getSubsequentType(final Scope scope) {
        final Set<Scope> successors = new HashSet<>(getSuccessors(scope));
        final Set<Scope> keywords = successors.stream()
                .filter(s -> ((NodeEdge) getEdge(scope, s)).getNode().getType().equals(NodeType.TYPE))
                .collect(Collectors.toSet());
        successors.removeAll(keywords);
        successors.forEach(s -> keywords.addAll(getSubsequentType(s)));
        return Collections.unmodifiableSet(keywords);
    }

    /**
     * Returns all scopes connected by any type of edge ingoing to this scope.
     *
     * @param scope Scope, not null, must be present in the graph.
     * @return Set of Scopes, not null.
     */
    public Set<Scope> getInGoingScopes(final Scope scope) {
        return incomingEdgesOf(scope).stream()
                .map(ScopeEdge::getSource)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns nodes that are before the given node in a directed graph.
     * Only scopes connected by a NodeEdge are considered a predecessor.
     *
     * @param scope Scope to search its predecessors.
     * @return List of nodes.
     */
    public Set<Scope> getPredecessors(final Scope scope) {
        return incomingEdgesOf(scope).stream()
                .filter(edge -> edge.getSource() != edge.getTarget())
                .filter(edge -> edge instanceof NodeEdge)
                .map(ScopeEdge::getSource)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the outgoing nodes, that are connected by a NodeEdge to the given scope.
     *
     * @param scope Scope, must not be null.
     * @return List of nodes.
     */
    public Set<Node> getOutGoingNodes(final Scope scope) {
        return outGoingNodeEdges(scope).stream()
                .map(NodeEdge::getNode)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the outgoing NodeEdges of the provided scope.
     *
     * @param scope Scope, must not be null and exist in the graph.
     * @return Set of NodeEdges, may be empty.
     */
    public Set<NodeEdge> outGoingNodeEdges(final Scope scope) {
        return outgoingEdgesOf(scope).stream()
                .filter(edge -> edge.getSource() != edge.getTarget())
                .filter(scopeEdge -> scopeEdge instanceof NodeEdge)
                .map(scopeEdge -> ((NodeEdge) scopeEdge))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns a list of ingoing nodes that are connected by a NodeEdge to the given scope.
     *
     * @param scope Scope, must not be null.
     * @return List of Nodes.
     */
    public Set<Node> getInGoingNodes(final Scope scope) {
        return incomingEdgesOf(scope).stream()
                .filter(scopeEdge -> scopeEdge instanceof NodeEdge)
                .map(nodeEdge -> ((NodeEdge) nodeEdge).getNode())
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the (first) node with no ingoing edges.
     *
     * @return A scope, if no scope found a NPE is thrown.
     */
    public Scope getStartScope() {
        BnfRuleGraph noBackEdgeGraph = copyWithoutBackwardEdges();
        final List<Scope> scopesWithoutIngoingEdges = noBackEdgeGraph.vertexSet().stream()
                .filter(o -> noBackEdgeGraph.inDegreeOf(o) == 0)
                .collect(Collectors.toList());
        if (scopesWithoutIngoingEdges.size() != 1) {
            throw new InvalidGraphException(
                    "There must be exactly one starting node! (found: " + scopesWithoutIngoingEdges.size() + ")");
        }
        return scopesWithoutIngoingEdges.get(0);
    }

    /**
     * Returns the (first) node with no outgoing edges.
     *
     * @return A scope, if no scope found a NPE is thrown.
     */
    public Scope getEndScope() {
        final BnfRuleGraph noBackEdgeGraph = copyWithoutBackwardEdges();
        final List<Scope> scopesWithoutOutgoingEdges = noBackEdgeGraph.vertexSet().stream()
                .filter(scope -> noBackEdgeGraph.outDegreeOf(scope) == 0)
                .collect(Collectors.toUnmodifiableList());
        if (scopesWithoutOutgoingEdges.size() != 1) {
            throw new InvalidGraphException(
                    "There must be exactly one end node! (found: " + scopesWithoutOutgoingEdges.size() + ")");
        }
        return scopesWithoutOutgoingEdges.get(0);
    }

    /**
     * Returns a Set containing the ScopeEdges of Nodes, that have no successor.
     *
     * @return Set of ScopeEdges, not null.
     */
    public Set<ScopeEdge> getDanglingScopeEdges() {
        return vertexSet().stream()
                .filter(scope -> outDegreeOf(scope) == 0)
                .flatMap(scope -> incomingEdgesOf(scope).stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns a Set containing the ScopeEdges of Nodes having no successor, but the Node must be successor of root.
     *
     * @param root A Node existing in the graph.
     * @return Set of ScopeEdges, not null.
     */
    public Set<ScopeEdge> getDanglingScopeEdges(final Scope root) {
        if (!vertexSet().contains(root)) {
            return Collections.emptySet();
        } else {
            return getDanglingNodesOf(root).stream()
                    .flatMap(scope -> incomingEdgesOf(scope).stream())
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    private Set<Scope> getDanglingNodesOf(final Scope scope) {
        final Set<Scope> dangling = new HashSet<>();
        for (Scope successor : getSuccessors(scope)) {
            if (outDegreeOf(successor) > 0) {
                dangling.addAll(getDanglingNodesOf(successor));
            } else {
                dangling.add(successor);
            }
        }
        return dangling;
    }

    /**
     * Returns true if the target node is closer to the end node than to the start node compared to the source node.
     *
     * @param edge Edge to be determined.
     * @return True if target node closer to end as source node.
     */
    public boolean isForwardEdge(final ScopeEdge edge) {
        final int distanceStartSource =
                DijkstraShortestPath.findPathBetween(this, getStartScope(), edge.getSource()).getLength();
        final int distanceStartTarget =
                DijkstraShortestPath.findPathBetween(this, getStartScope(), edge.getTarget()).getLength();
        return distanceStartTarget > distanceStartSource;
    }

    /**
     * Determines if the target node is closer to the start node than to the end node compared to the source node.
     *
     * @param edge Edge to be determined.
     * @return True if the target node is closer to the start node than the source, else false.
     */
    public boolean isBackwardEdge(final ScopeEdge edge) {
        final int distanceStartSource =
                DijkstraShortestPath.findPathBetween(this, getStartScope(), edge.getSource()).getLength();
        final int distanceStartTarget =
                DijkstraShortestPath.findPathBetween(this, getStartScope(), edge.getTarget()).getLength();
        return distanceStartTarget < distanceStartSource;
    }

    /**
     * Returns a copy of the graph containing no backwards edges.
     *
     * @return A new graph.
     */
    public BnfRuleGraph copyWithoutBackwardEdges() {
        final BnfRuleGraph onlyForwardEdges = new BnfRuleGraph(name);
        vertexSet().forEach(onlyForwardEdges::addVertex);
        getNodeEdges().stream()
                .filter(edge -> edge.getSource() != edge.getTarget())
                .forEach(edge -> onlyForwardEdges.addEdge(edge.getSource(), edge.getTarget(), edge));
        return onlyForwardEdges;
    }

    private boolean notConnectedByNodeEdges(final Scope start, final Scope end) {
        return DijkstraShortestPath.findPathBetween(copyWithoutBackwardEdges(), start, end) == null;
    }

    /**
     * Returns true if the graph contains at least one non-terminal node.
     *
     * @return True if non-terminal node is present.
     */
    public boolean containsNonTerminals() {
        return getNonTerminalNodes().size() > 0;
    }

    private Set<NodeEdge> getNodeEdges() {
        // Needs to be saved to a modifiable set and then converted to an unmodifiable set, because a non-terminal may
        // occur multiple times in a graph, producing an IllegalArgumentException on
        // collect(Collectors.toUnmodifiableSet())
        return Collections.unmodifiableSet(edgeSet().stream()
                .filter(edge -> edge instanceof NodeEdge)
                .map(edge -> ((NodeEdge) edge))
                .collect(Collectors.toSet()));
    }

    /**
     * Returns a subset of the edge set that contains only edges that contain non-terminal nodes.
     *
     * @return Set of NodeEdges, not null, may be empty.
     */
    public Set<NodeEdge> getNonTerminalNodeEdges() {
        // Needs to be saved to a modifiable set and then converted to an unmodifiable set, because a non-terminal may
        // occur multiple times in a graph, producing an IllegalArgumentException on
        // collect(Collectors.toUnmodifiableSet())
        return Collections.unmodifiableSet(getNodeEdges().stream()
                .filter(nodeEdge -> nodeEdge.getNode().getType().equals(NodeType.NON_TERMINAL))
                .collect(Collectors.toSet()));
    }

    /**
     * Returns the nodes of the graph's edges, that are non-terminal nodes.
     *
     * @return Set of Nodes, not null, may be empty.
     */
    public Set<Node> getNonTerminalNodes() {
        return getNonTerminalNodeEdges().stream()
                .map(NodeEdge::getNode)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Renders this graph to a DOT file.
     *
     * @param path File path in an existing directory.
     * @throws IOException Thrown if file could not be saved or directory does not exist.
     */
    public void renderBnfRuleGraph(final String path) throws IOException {
        try (PrintWriter fileWriter = new PrintWriter(path, StandardCharsets.UTF_8)) {
            fileWriter.println(toString());
        }
    }

    @Override
    public String toString() {
        final DOTExporter<Scope, ScopeEdge> exporter = new DOTExporter<>(Scope::getName);
        exporter.setEdgeIdProvider(edge -> getAttributeMap(edge).get("label").getValue());
        exporter.setEdgeAttributeProvider(BnfRuleGraph::getAttributeMap);
        final StringWriter writer = new StringWriter();
        exporter.exportGraph(this, writer);
        return writer.toString();
    }

    private static Map<String, Attribute> getAttributeMap(final ScopeEdge edge) {
        if (!(edge instanceof NodeEdge)) {
            return Map.of("label", new DefaultAttribute<>(edge.getClass().getSimpleName(), AttributeType.STRING));
        }
        final Node node = ((NodeEdge) edge).getNode();
        final String labelValue = node.getName() + " [" + node.getType().toString() + "]";
        return Map.of("label", new DefaultAttribute<>(labelValue, AttributeType.STRING));
    }
}
