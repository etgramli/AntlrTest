package de.etgramlich.util.graph.type;

import de.etgramlich.util.graph.type.node.Node;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.AsUnmodifiableGraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.ParanoidGraph;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class GraphWrapper {

    private final Graph<Scope, ScopeEdge> graph;
    private int scopeNumber = 0;
    private Scope lastAddedScope;

    /**
     * Creates a GraphWrapper with a directed pseudo graph with one node with the given name.
     * @param startScopeName Name of the first scope.
     */
    public GraphWrapper(final String startScopeName) {
        final Graph<Scope, ScopeEdge> tmpGraph = new DirectedPseudograph<>(null, null, false);
        graph = new ParanoidGraph<>(tmpGraph);
        lastAddedScope = new Scope(startScopeName);
        graph.addVertex(lastAddedScope);
    }

    /**
     * Copies the provided graph.
     * @param graph Graph to copy into the wrapper.
     */
    public GraphWrapper(final Graph<Scope, ScopeEdge> graph) {
        this.graph = new ParanoidGraph<>(new DirectedPseudograph<>(null, null, false));
        Graphs.addGraph(this.graph, graph);
        lastAddedScope = getEndScope();
    }

    public Graph<Scope, ScopeEdge> getGraph() {
        return new AsUnmodifiableGraph<>(graph);
    }

    public boolean isGraphConsistent() {
        // Empty graph is valid
        if (graph.edgeSet().isEmpty() && graph.vertexSet().isEmpty()) {
            return true;
        }
        // All source and target scopes in edge must exist in graph
        for (ScopeEdge edge : graph.edgeSet()) {
            if (!graph.vertexSet().contains(edge.getSource()) || !graph.vertexSet().contains(edge.getTarget())) {
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
     * Adds a Scope as vertex to the graph, and associates the node with the forward directed edge and also creates
     * also a second edge with null as Node, indicating that the node in the other edge can be omitted.
     * @param scope New vertex, must not be null.
     * @param node Node associated with the edge.
     */
    public void addOptional(final Node node) {
        final Scope newScope = getNextScope();
        graph.addVertex(newScope);

        graph.addEdge(lastAddedScope, newScope, new ScopeEdge(lastAddedScope, newScope, node));
        graph.addEdge(lastAddedScope, newScope, null);

        lastAddedScope = newScope;
    }

    /**
     * Adds a sequence of nodes (may be only one) to the graph.
     * @param scope Next scope to be added as edge.
     * @param node Node(s) to be associated with the edge.
     */
    public void addSequence(final Node node) {
        final Scope newScope = getNextScope();
        graph.addVertex(newScope);

        graph.addEdge(lastAddedScope, newScope, new ScopeEdge(lastAddedScope, newScope, node));

        lastAddedScope = newScope;
    }

    /**
     * Adds a Scope as vertex to the graph and associates the Node with the forward edge and also creates an Edge with
     * null as Node backwards.
     * @param scope Scope  to add as vertex.
     * @param loop Loop node to add to the forward edge.
     */
    public void addLoop(final Node loop) {
        final Scope newScope = getNextScope();
        graph.addVertex(newScope);

        graph.addEdge(lastAddedScope, newScope, new ScopeEdge(lastAddedScope, newScope, loop));
        graph.addEdge(newScope, lastAddedScope, null);

        lastAddedScope = newScope;
    }

    /**
     * Adds alternatives as individual edges to the Graph, from the last added scope to the passed one.
     * @param scope New added scope.
     * @param alternatives Alternatives to be added, must not be empty.
     */
    public void addAlternatives(final Collection<Node> alternatives) {
        assert (!alternatives.isEmpty());

        final Scope newScope = getNextScope();
        graph.addVertex(newScope);
        for (Node alternative : alternatives) {
            graph.addEdge(lastAddedScope, newScope, new ScopeEdge(lastAddedScope, newScope, alternative));
        }
        lastAddedScope = newScope;
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
            throw new InvalidGraphException(
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
            throw new InvalidGraphException(
                    "There must be exactly one end node! (found: " + scopesWithoutOutgoingEdges.size() + ")");
        }
        return scopesWithoutOutgoingEdges.get(0);
    }

    public List<Node> getOutGoingNodes(final Scope scope) {
        return graph.outgoingEdgesOf(scope).stream()
                .flatMap(scopeEdge -> scopeEdge.getNodes().stream())
                .collect(Collectors.toList());
    }

    public Scope getLastAddedScope() {
        return lastAddedScope;
    }

    private Scope getNextScope() {
        return new Scope("Scope_" + scopeNumber++);
    }
}
