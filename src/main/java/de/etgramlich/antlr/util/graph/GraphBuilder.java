package de.etgramlich.antlr.util.graph;

import de.etgramlich.antlr.parser.type.Rule;
import de.etgramlich.antlr.parser.type.RuleList;
import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.rhstype.Element;
import de.etgramlich.antlr.semanticmodel.scope.Scope;
import de.etgramlich.antlr.util.graph.node.Node;
import de.etgramlich.antlr.util.graph.node.SequenceNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class GraphBuilder {
    private final Graph<Node, BnfEdge> graph;

    private GraphBuilder(@NotNull final RuleList ruleList) {
        final List<Rule> nonTerminalRules = ruleList.getRules().stream().filter(rule -> !rule.isTerminal()).collect(Collectors.toList());
        //graph = new ParanoidGraph<>(DefaultEdge.class);
        // ToDo: Consider allowing cycles to allow repetitions
        graph = new DirectedAcyclicGraph<>(BnfEdge.class);

        for (Rule rule : nonTerminalRules) {
            addAlternatives(rule.getRhs());
        }
    }

    private void addAlternatives(final List<Alternative> alternatives) {
        // ToDo
    }

    private void addAlternative(final Alternative alternative) {
        // ToDo
    }

    private void addElement(final Element element, final Node predecessor) {
        Node node = null;
        if (element.isId()) {
            node = new SequenceNode(element.getId().getText());
        } else if (element.isAlternative()) {
            // TODo
        } else {// Is LetterRange

        }
        // ToDo
    }

    public static Scope toScope(final Node node) {
        // ToDo

        return null;
    }

    private Node findNodeByName(final String id) {
        return graph.vertexSet().stream().filter(bnfType -> bnfType.getName().equals(id)).findFirst().orElse(null);
    }

    @Nullable
    private Node findFollowing(final Node node) {
        final List<Node> following = findFollowings(node);
        if (following.isEmpty()) {
            return null;
        } else {
            return following.get(0);
        }
    }

    private List<Node> findFollowings(final Node node) {
        return graph.outgoingEdgesOf(node).stream().map(BnfEdge::getTarget).collect(Collectors.toList());
    }

    @Nullable
    private Node findPredecessor(final Node node) {
        final List<Node> predecessors = findPredecessors(node);
        return predecessors.isEmpty() ? null : predecessors.get(0);
    }

    private List<Node> findPredecessors(final Node node) {
        return graph.incomingEdgesOf(node).stream().map(BnfEdge::getSource).collect(Collectors.toList());
    }

    @NotNull
    private Node getStartNode() {
        Optional<Node> startNode = graph.vertexSet().stream().filter(o -> graph.inDegreeOf(o) == 0).findFirst();
        if (startNode.isEmpty()) {
            throw new NullPointerException("No starting node!");
        }
        return startNode.get();
    }

    @NotNull
    private Node getEndNode() {
        Optional<Node> endNode = graph.vertexSet().stream().filter(o -> graph.outDegreeOf(o) == 0).findFirst();
        if (endNode.isEmpty()) {
            throw new NullPointerException("No end node!");
        }
        return endNode.get();
    }
}
