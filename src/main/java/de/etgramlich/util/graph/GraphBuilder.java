package de.etgramlich.util.graph;

import com.google.common.collect.Lists;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a Graph with Scopes as vertices and Node as edges.
 * List of rules must be passed as constructor argument, getGraph can be passed on each constructed object.
 */
public final class GraphBuilder {
    private final GraphWrapper graphWrapper;

    private int scopeNumber = 0;
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
        graphWrapper = new GraphWrapper(startBnfRule.getName());
        nonTerminalBnfRules.remove(startBnfRule);

        for (BnfRule bnfRule : nonTerminalBnfRules) {
            processAlternatives(bnfRule.getRhs());
        }
    }

    /**
     * Adds the current node to the GraphWrapper, between the last added Scope and a new one with the given name.
     * @param nextScopeName Non-blank String containing the name of the next Scope in the graph.
     */
    private void addNode() {
        final Scope currentScope = new Scope(getNextScopeName());
        if (currentNode instanceof LoopNode) {
            graphWrapper.addLoop(currentScope, currentNode);
        } else if (currentNode instanceof SequenceNode) {
            if (currentNode.isOptional())  graphWrapper.addOptional(currentScope, currentNode);  // Optional
            else                           graphWrapper.addSequence(currentScope, currentNode);  // Sequence / one element
        } else if (currentNode instanceof AlternativeNode) {
            graphWrapper.addSequence(currentScope, currentNode);
        }
    }

    private void processAlternatives(@NotNull final Alternatives alternatives) {
        assert (!alternatives.getSequences().isEmpty());
        if (alternatives.getSequences().size() > 1) {
            currentNode = new AlternativeNode(alternatives.getSequences().get(0).getName());
            for (Sequence sequence : alternatives.getSequences()) {
                processSequence(sequence);
            }
            addNode();
        }
        // Alternative, Sequence, Optional, ZeroOrMore, Precedence, ID or LetterRange
        for (Sequence sequence : alternatives.getSequences()) {
            processSequence(sequence);
        }
    }
    private void processSequence(@NotNull final Sequence sequence) {
        assert (sequence.getElements().size() > 0);
        for (Element element : sequence.getElements()) {
            processElement(element);
        }
    }

    /**
     * Processes given element and adds it to the Graph.
     * @param element Element of EBNF grammar to be added, must not be null.
     */
    private void processElement(@NotNull final Element element) {
        // ToDo: In case of optional, zeroOrMore and precedence: make recursive
        if (element instanceof TextElement) { // Sequence
            // ToDo
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
     * Last Node of current Node (sequence).
     * @return (Current) Node or null, if currentNode is null.
     */
    private Node getLastOfSequence() {
        return getLastOfSequence(currentNode);
    }

    /**
     * Returns last Node of a sequence.
     * @param node Node, may be null.
     * @return Node (argument if it has no successor), or null if argument is null.
     */
    private static Node getLastOfSequence(final Node node) {
        if (node == null) {
            return null;
        }
        Node current = node;
        while (current.getSuccessor() != null) {
            current = current.getSuccessor();
        }
        return current;
    }

    private static SequenceNode getSequence(final List<Sequence> sequences) {
        SequenceNode sequenceNode = null;
        for (Sequence sequence : Lists.reverse(sequences)) {
            sequenceNode = new SequenceNode(sequence.getName(), sequenceNode);
        }
        return sequenceNode;
    }

    /**
     * Returns an unmodifiable view of the graph built by the constructor.
     * Throws unsupported operation exception when trying to modify the graph.
     * @return Unmodifiable graph.
     */
    public Graph<Scope, ScopeEdge> getGraph() {
        return graphWrapper.getGraph();
    }

    private String getNextScopeName() {
        return "Scope_" + scopeNumber++;
    }
}
