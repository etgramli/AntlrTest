package de.etgramlich.graph.type;

import de.etgramlich.util.exception.InvalidGraphException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Builds a graph according to the rules of a EBNF (has to care about optional elements and repetitions).
 */
public final class BnfRuleGraph extends DirectedPseudograph<Scope, ScopeEdge> {

    /**
     * Creates an unweighted BnfRuleGraph without vertex and edge supplier.
     */
    public BnfRuleGraph() {
        this(null, null);
    }

    /**
     * Creates an unweighted BnfRuleGraph with the provided vertex and edge supplier.
     * @param vertexSupplier Vertex supplier.
     * @param edgeSupplier Edge supplier.
     */
    public BnfRuleGraph(final Supplier<Scope> vertexSupplier, final Supplier<ScopeEdge> edgeSupplier) {
        super(vertexSupplier, edgeSupplier, false);
    }

    /**
     * Returns the length from the start node to end node.
     * @return Non-negative integer.
     */
    public int length() {
        if (isEmpty()) {
            return 0;
        }
        return DijkstraShortestPath.findPathBetween(this, getStartScope(), getEndScope()).getLength();
    }

    /**
     * Returns true if the graph contains no edges and no vertices.
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
     * @return True if the graph is consistent or empty.
     */
    public boolean isConsistent() {
        // Empty graph is valid
        if (edgeSet().isEmpty() && vertexSet().isEmpty()) {
            return true;
        }
        for (ScopeEdge edge : edgeSet()) {
            if (!vertexSet().contains(edge.getSource()) || !vertexSet().contains(edge.getTarget())) {
                return false;
            }
            // There must exist a parallel connection
            if (edge instanceof OptionalEdge && notConnectedByNodeEdges(edge.getSource(), edge.getTarget())) {
                return false;
            }
            // There must exist a parallel connection in reversed direction
            if (edge instanceof RepetitionEdge && notConnectedByNodeEdges(edge.getTarget(), edge.getSource())) {
                return false;
            }
        }
        // Only one start and end scope are permitted
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
     * @param scope Scope, must not be null and present in graph.
     * @return Set of scopes, not null, may be empty.
     */
    public Set<Scope> getOutGoingScopes(final Scope scope) {
        return outgoingEdgesOf(scope).stream()
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
     * Returns all scopes connected by any type of edge ingoing to this scope.
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
     * Only scopes connected by a NodeEdge are considered predecessor.
     *
     * @param scope Scope to search its predecessors.
     * @return List of nodes.
     */
    public Set<Scope> getPredecessors(final Scope scope) {
        return incomingEdgesOf(scope).stream()
                .filter(edge -> edge instanceof NodeEdge)
                .map(ScopeEdge::getSource)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the outgoing nodes, that are connected by a NodeEdge to the given scope.
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
     * @param scope Scope, must not be null and exist in the graph.
     * @return Set of NodeEdges, may be empty.
     */
    public Set<NodeEdge> outGoingNodeEdges(final Scope scope) {
        return outgoingEdgesOf(scope).stream()
                .filter(scopeEdge -> scopeEdge instanceof NodeEdge)
                .map(scopeEdge -> ((NodeEdge) scopeEdge))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns a list of ingoing nodes that are connected by a NodeEdge to the given scope.
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
                .collect(Collectors.toSet());
    }

    /**
     * Returns a Set containing the ScopeEdges of Nodes having no successor, but the Node must be successor of root.
     * @param root A Node existing in the graph.
     * @return Set of ScopeEdges, not null.
     */
    public Set<ScopeEdge> getDanglingScopeEdges(final Scope root) {
        if (!vertexSet().contains(root)) {
            return Collections.emptySet();
        }

        return getDanglingNodesOf(root).stream()
                .flatMap(scope -> incomingEdgesOf(scope).stream())
                .collect(Collectors.toSet());
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

    private BnfRuleGraph copyWithoutBackwardEdges() {
        final BnfRuleGraph noBackEdges = (BnfRuleGraph) this.clone();
        noBackEdges.removeAllEdges(noBackEdges.edgeSet().stream()
                .filter(edge -> edge instanceof OptionalEdge || edge instanceof RepetitionEdge)
                .collect(Collectors.toUnmodifiableSet()));
        return noBackEdges;
    }

    private boolean notConnectedByNodeEdges(final Scope start, final Scope end) {
        final GraphPath<Scope, ScopeEdge> shortestPath =
                DijkstraShortestPath.findPathBetween(copyWithoutBackwardEdges(), start, end);
        return shortestPath == null;
    }
}
