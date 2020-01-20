package de.etgramlich.util.graph.type;

import de.etgramlich.util.graph.type.node.Node;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.jgrapht.graph.AsUnmodifiableGraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.ParanoidGraph;

import java.util.List;
import java.util.stream.Collectors;

public final class GraphWrapper {

    private final Graph<Scope, ScopeEdge> graph;
    private Scope lastAddedScope;

    public GraphWrapper() {
        final Graph<Scope, ScopeEdge> tmpGraph = new DirectedPseudograph<>(null, null, false);
        graph = new ParanoidGraph<>(tmpGraph);
        lastAddedScope = null;
    }

    public GraphWrapper(final String startScopeName) {
        final Graph<Scope, ScopeEdge> tmpGraph = new DirectedPseudograph<>(null, null, false);
        graph = new ParanoidGraph<>(tmpGraph);
        lastAddedScope = new Scope(startScopeName);
        graph.addVertex(lastAddedScope);
    }

    public Graph<Scope, ScopeEdge> getGraph() {
        return new AsUnmodifiableGraph<>(graph);
    }

    public boolean isGraphConsistent() {
        if (graph.edgeSet().isEmpty() && graph.vertexSet().isEmpty()) {
            return true;
        }
        try {
            getStartScope();
            getEndScope();
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Adds a Scope as vertex to the graph, and associates the node with the forward directed edge and also creates
     * also a second edge with null as Node, indicating that the node in the other edge can be omitted.
     * @param scope New vertex, must not be null.
     * @param node Node associated with the edge.
     */
    public void addOptional(@NotNull final Scope scope, @NotNull final Node node) {
        graph.addVertex(scope);
        if (lastAddedScope != null) {
            graph.addEdge(lastAddedScope, scope, new ScopeEdge(lastAddedScope, scope, node));
            graph.addEdge(lastAddedScope, scope, null);
        }
        lastAddedScope = scope;
    }

    /**
     * Adds a sequence of nodes (may be only one) to the graph.
     * @param scope Next scope to be added as edge.
     * @param node Node(s) to be associated with the edge.
     */
    public void addSequence(@NotNull final Scope scope, @NotNull final Node node) {
        graph.addVertex(scope);
        if (lastAddedScope != null) {
            graph.addEdge(lastAddedScope, scope, new ScopeEdge(lastAddedScope, scope, node));
        }
        lastAddedScope = scope;
    }

    /**
     * Adds a Scope as vertex to the graph and associates the Node with the forward edge and also creates an Edge with
     * null as Node backwards.
     * @param scope Scope  to add as vertex.
     * @param loop Loop node to add to the forward edge.
     */
    public void addLoop(@NotNull final Scope scope, @NotNull final Node loop) {
        graph.addVertex(scope);
        if (lastAddedScope != null) {
            graph.addEdge(lastAddedScope, scope, new ScopeEdge(lastAddedScope, scope, loop));
            graph.addEdge(scope, lastAddedScope, null);
        }
        lastAddedScope = scope;
    }

    /**
     * Returns the successors of a node in the directed graph.
     * @param scope Scope to find its successors.
     * @return List of Scopes.
     */
    public List<Scope> getSuccessors(final Scope scope) {
        return graph.outgoingEdgesOf(scope).stream().map(ScopeEdge::getTarget).collect(Collectors.toList());
    }

    /**
     * Returns nodes that are before the given node in a directed graph.
     * @param scope Scope to search its predecessors.
     * @return List of nodes.
     */
    public List<Scope> getPredecessors(@NotNull final Scope scope) {
        return graph.incomingEdgesOf(scope).stream().map(ScopeEdge::getSource).collect(Collectors.toList());
    }

    /**
     * Returns the (first) node with no ingoing edges.
     * @return A scope, if no scope found a NPE is thrown.
     */
    public Scope getStartScope() {
        final List<Scope> scopesWithoutIngoingEdges =
                graph.vertexSet().stream().filter(o -> graph.inDegreeOf(o) == 0).collect(Collectors.toList());
        if (scopesWithoutIngoingEdges.size() != 1) {
            throw new NullPointerException(
                    "There must be exactly one starting node! (found: " + scopesWithoutIngoingEdges.size() + ")");
        }
        return scopesWithoutIngoingEdges.get(0);
    }

    /**
     * Returns the (first) node with no outgoing edges.
     * @return A scope, if no scope found a NPE is thrown.
     */
    public Scope getEndScope() {
        final List<Scope> scopesWithoutOutgoingEdges =
                graph.vertexSet().stream().filter(o -> graph.outDegreeOf(o) == 0).collect(Collectors.toList());
        if (scopesWithoutOutgoingEdges.size() != 1) {
            throw new NullPointerException(
                    "There must be exactly one end node! (found: " + scopesWithoutOutgoingEdges.size() + ")");
        }
        return scopesWithoutOutgoingEdges.get(0);
    }
}
