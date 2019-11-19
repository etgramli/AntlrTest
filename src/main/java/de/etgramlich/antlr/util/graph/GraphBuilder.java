package de.etgramlich.antlr.util.graph;

import com.google.common.collect.Lists;
import de.etgramlich.antlr.parser.type.Rule;
import de.etgramlich.antlr.parser.type.RuleList;
import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.rhstype.Element;
import de.etgramlich.antlr.util.CollectionUtil;
import de.etgramlich.antlr.util.graph.node.AlternativeNode;
import de.etgramlich.antlr.util.graph.node.Node;
import de.etgramlich.antlr.util.graph.node.SequenceNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgrapht.Graph;
import org.jgrapht.graph.AsUnmodifiableGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.ParanoidGraph;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class GraphBuilder {
    private final Graph<Scope, BnfEdge> graph;
    private Scope lastAddedScope;

    public GraphBuilder(@NotNull final RuleList ruleList) {
        final Graph<Scope, BnfEdge> tmpGraph = new DefaultDirectedGraph<>(BnfEdge.class);
        graph = new ParanoidGraph<>(tmpGraph);
        lastAddedScope = null;

        final List<Rule> nonTerminalRules = ruleList.getRules().stream().filter(rule -> !rule.isTerminal()).collect(Collectors.toList());
        nonTerminalRules.forEach(rule -> addAlternatives(rule.getRhs()));
    }

    /**
     * Adds scope to the graph and links it to the previously added scope.
     * The edge is directional from the previous scope to the passed scope.
     * @param scope The new scope to add, it will be the next end scope.
     */
    private void addNodeAndVertexToLastAddedNode(final Scope scope) {
        graph.addVertex(scope);
        if (lastAddedScope != null) {
            graph.addEdge(lastAddedScope, scope, new BnfEdge(lastAddedScope, scope));
        }
        lastAddedScope = scope;
    }

    /**
     * Returns an unmodifiable view of the graph built by the constructor.
     * Throws unsupported operation exception when trying to modify the graph.
     * @return Unmodifiable graph.
     */
    @NotNull
    @Contract(pure = true)
    public Graph<Scope, BnfEdge> getGraph() {
        return new AsUnmodifiableGraph<>(graph);
    }

    /**
     * Adds a Scope with alternatives to the graph.
     * @param alternatives Alternatives list, must not be null and not empty.
     */
    private void addAlternatives(@NotNull final List<Alternative> alternatives) {
        if (alternatives.isEmpty()) {
            throw new IllegalArgumentException("Alternatives is empty!!");
        }
        final Scope scope = new Scope(alternatives.get(0).getName());
        for (Alternative alternative : alternatives) {
            scope.addNode(getAlternative(alternative));
        }
        addNodeAndVertexToLastAddedNode(scope);
    }

    /**
     * Get alternatives contained in one AlternativeNode
     * @param alternatives List of alternatives, must not be null.
     * @return An AlternativeNode, never null.
     */
    @NotNull
    private AlternativeNode getAlternatives(@NotNull final List<Alternative> alternatives) {
        List<SequenceNode> alternativesNodes = alternatives.stream().map(this::getAlternative).collect(Collectors.toList());
        return new AlternativeNode(alternatives.get(0).getName(), alternativesNodes);
    }

    /**
     * Adds alternative in a Scope to the graph.
     * @param alternative Alternative to be added to the scope.
     */
    private void addAlterative(@NotNull Alternative alternative) {
        Scope alternativeScope = new Scope(alternative.getName(), CollectionUtil.toList(getAlternative(alternative)));
    }

    /**
     * Returns a sequence of Sequence of nodes for the elements of the given Alternative (bnf).
     * If there is only one element in the alternative, the first SequenceNode's successor is null.
     * @param alternative Non-Null Alternative with at least 1 element.
     * @return SequenceNode, not null.
     */
    private SequenceNode getAlternative(@NotNull final Alternative alternative) {
        SequenceNode alternativeNode = null;
        final int numElements = alternative.getElements().size();
        if (numElements == 1) {
            alternativeNode = new SequenceNode(alternative.getName());
        } else if (numElements > 1) {
            for (Element element : Lists.reverse(alternative.getElements())) {
                alternativeNode = new SequenceNode(element.getName(), alternativeNode);
            }
        } else {
            throw new IllegalArgumentException("Alternatives with no elements!");
        }
        return alternativeNode;
    }

    private void addElement(final Element element) {
        // ToDo
    }

    private Node getElement(final Element element) {
        Node node = null;
        if (element.isId()) {
            node = new SequenceNode(element.getId().getText());
        } else if (element.isAlternative()) {
            // TODo
        } else {// Is LetterRange
            // ToDo
        }
        return node;
    }

    /**
     * Finds a scope with the given name.
     * @param id Name of the scope.
     * @return Scope or null, if no scope exists with the given name.
     */
    private Scope findScopeByName(final String id) {
        return graph.vertexSet().stream().filter(scope -> scope.getName().equals(id)).findFirst().orElse(null);
    }

    /**
     * Returns first successor of the given node.
     * @param scope Scope to find its successor.
     * @return Scope or null, if there is no successor.
     */
    @Nullable
    private Scope findSuccessor(final Scope scope) {
        final List<Scope> following = findSuccessors(scope);
        return following.isEmpty() ? null : following.get(0);
    }

    /**
     * Returns the successors of a node in the directed graph.
     * @param scope Scope to find its successors.
     * @return List of Scopes.
     */
    private List<Scope> findSuccessors(final Scope scope) {
        return graph.outgoingEdgesOf(scope).stream().map(BnfEdge::getTarget).collect(Collectors.toList());
    }

    /**
     * Finds the first predecessor for the given scope.
     * @param scope Scope to finds its predecessor.
     * @return Scope or null, if it has no predecessor.
     */
    @Nullable
    private Scope findPredecessor(final Scope scope) {
        final List<Scope> predecessors = findPredecessors(scope);
        return predecessors.isEmpty() ? null : predecessors.get(0);
    }

    /**
     * Returns nodes that are before the given node in a directed graph.
     * @param scope Scope to search its predecessors.
     * @return List of nodes.
     */
    private List<Scope> findPredecessors(@NotNull final Scope scope) {
        return graph.incomingEdgesOf(scope).stream().map(BnfEdge::getSource).collect(Collectors.toList());
    }

    /**
     * Returns the (first) node with no ingoing edges.
     * @return A scope, if no scope found a NPE is thrown.
     */
    @NotNull
    private Scope getStartScope() {
        Optional<Scope> startScope = graph.vertexSet().stream().filter(o -> graph.inDegreeOf(o) == 0).findFirst();
        if (startScope.isEmpty()) {
            throw new NullPointerException("No starting node!");
        }
        return startScope.get();
    }

    /**
     * Returns the (first) node with no outgoing edges.
     * @return A scope, if no scope found a NPE is thrown.
     */
    @NotNull
    private Scope getEndNode() {
        Optional<Scope> endNode = graph.vertexSet().stream().filter(o -> graph.outDegreeOf(o) == 0).findFirst();
        if (endNode.isEmpty()) {
            throw new NullPointerException("No end node!");
        }
        return endNode.get();
    }
}
