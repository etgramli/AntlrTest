package de.etgramlich.util.graph.type;

import de.etgramlich.util.exception.InvalidGraphException;
import de.etgramlich.util.graph.type.node.Node;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class BnfRuleGraph extends DirectedPseudograph<Scope, ScopeEdge> {

    public BnfRuleGraph() {
        this(null, null, false);
    }

    public BnfRuleGraph(Supplier<Scope> vertexSupplier, Supplier<ScopeEdge> edgeSupplier, boolean weighted) {
        super(vertexSupplier, edgeSupplier, weighted);
    }


    public int length() {
        if (isEmpty()) {
            return 0;
        }
        return DijkstraShortestPath.findPathBetween(this, getStartScope(), getEndScope()).getLength();
    }

    private boolean isEmpty() {
        return edgeSet().isEmpty() && vertexSet().isEmpty();
    }

    public boolean isConsistent() {
        // Empty graph is valid
        if (edgeSet().isEmpty() && vertexSet().isEmpty()) {
            return true;
        }
        // All source and target scopes in edge must exist in graph
        for (ScopeEdge edge : edgeSet()) {
            if (!vertexSet().contains(edge.getSource())) {
                System.err.println("Source Scope not in graph: " + edge.getSource().getName());
                return false;
            }
            if (!vertexSet().contains(edge.getTarget())) {
                System.err.println("Target Scope not in graph: " + edge.getTarget().getName());
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
     * Returns the successors of a node in the directed graph.
     *
     * @param scope Scope to find its successors.
     * @return List of Scopes.
     */
    public List<Scope> getSuccessors(final Scope scope) {
        return outgoingEdgesOf(scope).stream()
                .map(ScopeEdge::getTarget)
                .collect(Collectors.toList());
    }

    /**
     * Returns nodes that are before the given node in a directed graph.
     *
     * @param scope Scope to search its predecessors.
     * @return List of nodes.
     */
    public List<Scope> getPredecessors(@NotNull final Scope scope) {
        return incomingEdgesOf(scope).stream()
                .map(ScopeEdge::getSource)
                .collect(Collectors.toList());
    }

    public List<Node> getOutGoingNodes(final Scope scope) {
        return outgoingEdgesOf(scope).stream()
                .flatMap(scopeEdge -> scopeEdge.getNodes().stream())
                .collect(Collectors.toList());
    }

    public List<Node> getInGoingNodes(final Scope scope) {
        return edgeSet().stream()
                .filter(scopeEdge -> scopeEdge.getTarget().equals(scope))
                .flatMap(scopeEdge -> scopeEdge.getNodes().stream())
                .collect(Collectors.toList());
    }

    /**
     * Returns the (first) node with no ingoing edges.
     *
     * @return A scope, if no scope found a NPE is thrown.
     */
    public Scope getStartScope() {
        final List<Scope> scopesWithoutIngoingEdges = vertexSet().stream()
                .filter(o -> inDegreeOf(o) == 0)
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
        // Remove backward edges in own view
        final BnfRuleGraph noBackEdges = (BnfRuleGraph) this.clone();
        final Set<ScopeEdge> backwardEdges = noBackEdges.edgeSet().stream()
                .filter(this::isBackwardEdge)
                .collect(Collectors.toUnmodifiableSet());
        backwardEdges.forEach(noBackEdges::removeEdge);

        final List<Scope> scopesWithoutOutgoingEdges = noBackEdges.vertexSet().stream()
                .filter(scope -> outDegreeOf(scope) == 0)
                .collect(Collectors.toList());
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
        final Set<Scope> scopesWithoutSuccessor = vertexSet().stream()
                .filter(scope -> outDegreeOf(scope) == 0)
                .collect(Collectors.toUnmodifiableSet());
        return scopesWithoutSuccessor.stream()
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

    private List<Scope> getDanglingNodesOf(final Scope scope) {
        List<Scope> dangling = new ArrayList<>();
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
        final int lengthSource = DijkstraShortestPath.findPathBetween(this, getStartScope(), edge.getSource()).getLength();
        final int lengthTarget = DijkstraShortestPath.findPathBetween(this, getStartScope(), edge.getTarget()).getLength();
        return lengthTarget > lengthSource;
    }

    /**
     * Determines if the target node is closer to the start node than to the end node compared to the source node.
     * @param edge Edge to be determined.
     * @return True if the target node is closer to the start node than the source, else false.
     */
    public boolean isBackwardEdge(final ScopeEdge edge) {
        final int lengthSource = DijkstraShortestPath.findPathBetween(this, getStartScope(), edge.getSource()).getLength();
        final int lengthTarget = DijkstraShortestPath.findPathBetween(this, getStartScope(), edge.getTarget()).getLength();
        return lengthTarget < lengthSource;
    }
}
