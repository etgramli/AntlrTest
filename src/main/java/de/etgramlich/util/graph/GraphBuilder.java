package de.etgramlich.util.graph;

import de.etgramlich.parser.type.*;
import de.etgramlich.parser.type.repetition.AbstractRepetition;
import de.etgramlich.parser.type.text.TextElement;
import de.etgramlich.util.graph.type.GraphWrapper;
import de.etgramlich.util.graph.type.Scope;
import de.etgramlich.util.graph.type.ScopeEdge;
import de.etgramlich.util.graph.type.node.AlternativeNode;
import de.etgramlich.util.graph.type.node.LoopNode;
import de.etgramlich.util.graph.type.node.Node;
import de.etgramlich.util.graph.type.node.SequenceNode;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a Graph with Scopes as vertices and Node as edges.
 * List of rules must be passed as constructor argument, getGraph can be passed on each constructed object.
 */
public final class GraphBuilder {
    private final GraphWrapper graphWrapper;

    private Node currentNode = null;

    public GraphBuilder(@NotNull final Bnf bnf) {
        final List<BnfRule> startBnfRules = bnf.getBnfRules().stream().filter(BnfRule::isStartRule).collect(Collectors.toList());
        if (startBnfRules.size() != 1) {
            throw new IllegalArgumentException("Rule-list must have exactly one Start rule");
        }
        final BnfRule startBnfRule = startBnfRules.get(0);
        final List<BnfRule> nonTerminalBnfRules = bnf.getBnfRules().stream().filter(
                bnfRule -> !bnfRule.isTerminal() && bnfRule.getNumberOfElements() > 1
        ).collect(Collectors.toList());
        nonTerminalBnfRules.remove(startBnfRule);

        graphWrapper = new GraphWrapper(nonTerminalBnfRules.get(0).getLhs().getName());
        for (BnfRule bnfRule : nonTerminalBnfRules) {
            processAlternatives(bnfRule.getRhs());
            addNode();
        }
    }

    /**
     * Adds the current node to the GraphWrapper, between the last added Scope and a new one with the given name.
     * @param nextScopeName Non-blank String containing the name of the next Scope in the graph.
     */
    private void addNode() {
        addNode(currentNode);
    }

    private void addNode(final Node node) {
        if (node instanceof LoopNode) {
            graphWrapper.addLoop(node);
        } else if (node instanceof SequenceNode) {
            if (node.isOptional())  graphWrapper.addOptional(node);  // Optional
            else                    graphWrapper.addSequence(node);  // Sequence / one element
        } else if (node instanceof AlternativeNode) {
            graphWrapper.addSequence(node);
        }
    }

    private void processAlternatives(@NotNull final Alternatives alternatives) {
        assert (!alternatives.getSequences().isEmpty());

        final List<Node> alternativeNodes = new ArrayList<>(alternatives.getSequences().size());
        for (Sequence sequence : alternatives.getSequences()) {
            processSequence(sequence);
            alternativeNodes.add(new AlternativeNode(sequence.getName()));
        }
        graphWrapper.addAlternatives(alternativeNodes);
    }
    private void processSequence(@NotNull final Sequence sequence) {
        assert (sequence.getElements().size() > 0);
        for (Element element : sequence.getElements()) {
            processElement(element);
            graphWrapper.addSequence(new SequenceNode(element.getName()));
        }
    }

    /**
     * Processes given element and adds it to the Graph.
     * @param element Element of EBNF grammar to be added, must not be null.
     */
    private void processElement(@NotNull final Element element) {
        // ToDo
        if (element instanceof TextElement) {
            addNode();
            currentNode = new SequenceNode(element.getName());
        } else if (element instanceof AbstractRepetition) {
            AbstractRepetition repetition = (AbstractRepetition) element;
            for (Sequence sequence : repetition.getAlternatives().getSequences()) {
                processSequence(sequence);
            }
        } else {
            System.err.println("Unknown Element type... Should not happen!");
        }
    }

    /**
     * Returns an unmodifiable view of the graph built by the constructor.
     * Throws unsupported operation exception when trying to modify the graph.
     * @return Unmodifiable graph.
     */
    public Graph<Scope, ScopeEdge> getGraph() {
        return graphWrapper.getGraph();
    }
}
