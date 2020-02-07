package de.etgramlich.util.graph.type;

import de.etgramlich.util.exception.InvalidGraphException;
import de.etgramlich.util.graph.type.node.Node;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.graph.DirectedPseudograph;

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


    public boolean isConsistent() {
        // Empty graph is valid
        if (edgeSet().isEmpty() && vertexSet().isEmpty()) {
            return true;
        }
        // All source and target scopes in edge must exist in graph
        for (ScopeEdge edge : edgeSet()) {
            if (!vertexSet().contains(edge.getSource()) || !vertexSet().contains(edge.getTarget())) {
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
        final List<Scope> scopesWithoutOutgoingEdges = vertexSet().stream()
                .filter(scope -> outDegreeOf(scope) == 0)
                .collect(Collectors.toList());
        if (scopesWithoutOutgoingEdges.size() != 1) {
            throw new InvalidGraphException(
                    "There must be exactly one end node! (found: " + scopesWithoutOutgoingEdges.size() + ")");
        }
        return scopesWithoutOutgoingEdges.get(0);
    }

    public Set<ScopeEdge> getDanglingNodeEdges() {
        final Set<Scope> scopesWithoutSuccessor = vertexSet().stream()
                .filter(scope -> outDegreeOf(scope) == 0)
                .collect(Collectors.toUnmodifiableSet());
        return scopesWithoutSuccessor.stream()
                .flatMap(scope -> incomingEdgesOf(scope).stream())
                .collect(Collectors.toSet());
    }
}
