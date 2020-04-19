package de.etgramlich.dsl.graph.type;

import de.etgramlich.dsl.util.StringUtil;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Builds a graph according to the rules of a EBNF (has to care about optional elements and repetitions).
 */
public final class BnfRuleGraph extends DirectedPseudograph<Scope, ScopeEdge> {
    private static final long serialVersionUID = 8785829155769370692L;

    /**
     * Scope representing the entry point of the rule.
     */
    private Scope startScope;

    /**
     * Scope representing the end of the rule.
     */
    private Scope endScope;

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
        this.startScope = null;
        this.endScope = null;
    }

    /**
     * Returns the length from the start node to end node.
     *
     * @return Non-negative integer.
     */
    public int length() {
        return isEmpty() ? 0 : DijkstraShortestPath.findPathBetween(this, startScope, endScope).getLength();
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
        // Empty Graph is valid
        if (edgeSet().isEmpty() && vertexSet().isEmpty()) {
            return startScope == null && endScope == null;
        }
        // Graph must have one start scope with no ingoing edges
        if (startScope == null || !vertexSet().contains(startScope) || inDegreeOf(startScope) > 0) {
            return false;
        }
        // Graph must have one end scope with no outgoing edges
        if (endScope == null || !vertexSet().contains(endScope) || outDegreeOf(endScope) > 0) {
            return false;
        }

        return !notConnectedByEdges(startScope, endScope);
    }

    @Override
    public ScopeEdge addEdge(final Scope sourceVertex, final Scope targetVertex) {
        throw new UnsupportedOperationException("No edge supplier available! You must supply an already created edge.");
    }

    @Override
    public boolean addEdge(final Scope sourceVertex, final Scope targetVertex, final ScopeEdge scopeEdge) {
        if (scopeEdge instanceof OptionalEdge && sourceVertex == targetVertex) {
            throw new IllegalArgumentException("For an OptionalEdge the source and target vertices must be different!");
        }
        return super.addEdge(sourceVertex, targetVertex, scopeEdge);
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
        return outGoingNodeEdges(scope, false).stream()
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
        final Map<Boolean, List<Scope>> keywordMap = getPredecessors(scope).stream().collect(Collectors
                .partitioningBy(s -> ((NodeEdge) getEdge(s, scope)).getNode().getType().equals(NodeType.KEYWORD)));
        keywordMap.get(Boolean.FALSE).forEach(s -> keywordMap.get(Boolean.TRUE).addAll(getPrecedingKeywords(s)));
        return Set.copyOf(keywordMap.get(Boolean.TRUE));
    }

    /**
     * Get the closest following scopes following a NodeEdge containing a TYPE node.
     * @param scope Scope, must not be null, must be in the graph.
     * @return Set of Scopes, not null, may be empty.
     */
    public Set<Scope> getSubsequentType(final Scope scope) {
        final Map<Boolean, List<Scope>> typeMap = getSuccessors(scope).stream()
                .filter(s -> s != scope).collect(Collectors
                        .partitioningBy(s -> ((NodeEdge) getEdge(scope, s)).getNode().getType().equals(NodeType.TYPE)));
        final Set<Scope> types = new HashSet<>(typeMap.get(Boolean.TRUE));
        typeMap.get(Boolean.FALSE).stream()
                .flatMap(s -> outGoingNodeEdges(s, false).stream())
                .filter(edge -> edge.getNode().getType().equals(NodeType.TYPE))
                .map(ScopeEdge::getTarget)
                .forEach(types::add);
        return Collections.unmodifiableSet(types);
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
     * @param scope Scope, must not be null and be present in the graph.
     * @param selfEdges True if edges from and to the same scope should be included.
     * @return Set of nodes, not null, may be empty.
     */
    public Set<Node> getOutGoingNodes(final Scope scope, final boolean selfEdges) {
        return outGoingNodeEdges(scope, selfEdges).stream()
                .map(NodeEdge::getNode)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the outgoing nodes, that are connected by a NodeEdge to the given scope (edges with same source and
     * target not included).
     * @param scope Scope, must not be null and be present in the graph.
     * @return Set of nodes, not null, may be empty.
     */
    public Set<Node> getOutGoingNodes(final Scope scope) {
        return getOutGoingNodes(scope, false);
    }

    /**
     * Returns the outgoing NodeEdges of the provided scope.
     * @param scope Scope, must not be null and exist in the graph.
     * @param selfEdges True if edges from and to the same node should be included.
     * @return Set of NodeEdges, may be empty.
     */
    public Set<NodeEdge> outGoingNodeEdges(final Scope scope, final boolean selfEdges) {
        return outgoingEdgesOf(scope).stream()
                .filter(scopeEdge -> scopeEdge instanceof NodeEdge)
                .map(scopeEdge -> ((NodeEdge) scopeEdge))
                .filter(edge -> selfEdges || edge.getSource() != edge.getTarget())
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the outgoing NodeEdges of the provided scope (edges from and to same node not included).
     * @param scope Scope, must not be null and exist in the graph.
     * @return Set of NodeEdges, may be empty.
     */
    public Set<NodeEdge> outGoingNodeEdges(final Scope scope) {
        return outGoingNodeEdges(scope, false);
    }

    /**
     * Query the OptionalEdges that have the scope as source.
     * @param scope Scope, must not be null, must be present in the graph.
     * @return Set, not null, may be empty.
     */
    public Set<OptionalEdge> outGoingOptionalEdges(final Scope scope) {
        return outgoingEdgesOf(scope).stream()
                .filter(scopeEdge -> scopeEdge instanceof OptionalEdge)
                .map(scopeEdge -> (OptionalEdge) scopeEdge)
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
        return startScope;
    }

    /**
     * Sets the new start scope.
     * @param newStartScope Scope, must be not null and be in the graph.
     */
    public void setStartScope(final Scope newStartScope) {
        if (newStartScope == null) {
            throw new IllegalArgumentException("Start scope must not be null!");
        }
        if (!containsVertex(newStartScope)) {
            throw new IllegalArgumentException("Start scope must not be null!");
        }
        startScope = newStartScope;
    }

    /**
     * Returns the (first) node with no outgoing edges.
     *
     * @return A scope, if no scope found a NPE is thrown.
     */
    public Scope getEndScope() {
        return endScope;
    }

    /**
     * Sets new end scope.
     * @param newEndScope Scope, must not be null and in the graph.
     */
    public void setEndScope(final Scope newEndScope) {
        if (newEndScope == null) {
            throw new IllegalArgumentException("End scope must not be null!");
        }
        if (!containsVertex(newEndScope)) {
            throw new IllegalArgumentException("End scope must not be null!");
        }
        endScope = newEndScope;
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
            return getDanglingNodesOf(root, new HashSet<>()).stream()
                    .flatMap(scope -> incomingEdgesOf(scope).stream())
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    private Set<Scope> getDanglingNodesOf(final Scope scope, final Set<Scope> danglingScopes) {
        for (Scope successor : getSuccessors(scope)) {
            if (outDegreeOf(successor) > 0) {
                getDanglingNodesOf(successor, danglingScopes);
            } else {
                danglingScopes.add(successor);
            }
        }
        return danglingScopes;
    }

    /**
     * Returns true if the target node is closer to the end node than to the start node compared to the source node.
     *
     * @param edge Edge to be determined.
     * @return True if target node closer to end as source node.
     */
    public boolean isForwardEdge(final ScopeEdge edge) {
        final int distanceStartSource =
                DijkstraShortestPath.findPathBetween(this, startScope, edge.getSource()).getLength();
        final int distanceStartTarget =
                DijkstraShortestPath.findPathBetween(this, startScope, edge.getTarget()).getLength();
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
                DijkstraShortestPath.findPathBetween(this, startScope, edge.getSource()).getLength();
        final int distanceStartTarget =
                DijkstraShortestPath.findPathBetween(this, startScope, edge.getTarget()).getLength();
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
        edgeSet().stream()
                .filter(edge -> edge.getSource() != edge.getTarget())
                .filter(this::isForwardEdge)
                .forEach(edge -> onlyForwardEdges.addEdge(edge.getSource(), edge.getTarget(), edge));
        return onlyForwardEdges;
    }

    private boolean notConnectedByEdges(final Scope start, final Scope end) {
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

    /**
     * Generates a readable name for the provided scope.
     * @param scope Scope, must not be null, must be present in the graph.
     * @return String, not null, may be empty.
     */
    public String getReadableString(final Scope scope) {
        if (scope == getStartScope()) {
            return "BeginScope";
        }
        if (scope == getEndScope()) {
            return "EndScope";
        }
        final Set<String> nodeEdgeKeywordNames = outgoingEdgesOf(scope).stream()
                .filter(edge -> edge instanceof NodeEdge)
                .map(edge -> (NodeEdge) edge)
                .filter(edge -> edge.getNode().getType().equals(NodeType.KEYWORD))
                .map(edge -> edge.getNode().getName())
                .collect(Collectors.toUnmodifiableSet());
        if (!nodeEdgeKeywordNames.isEmpty()) {
            return nodeEdgeKeywordNames.stream()
                    .map(StringUtil::firstCharToUpperCase)
                    .sorted()
                    .collect(Collectors.joining())
                    + "Scope";
        } else {
            return scope.getName();
        }
    }

    @Override
    public String toString() {
        final DOTExporter<Scope, ScopeEdge> exporter = new DOTExporter<>(Scope::getName);
        exporter.setEdgeIdProvider(edge -> edge.getAttributeMap().get("label").getValue());
        exporter.setEdgeAttributeProvider(ScopeEdge::getAttributeMap);
        final StringWriter writer = new StringWriter();
        exporter.exportGraph(this, writer);
        return writer.toString();
    }
}
