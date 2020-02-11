package de.etgramlich.util.graph;

import de.etgramlich.parser.type.*;
import de.etgramlich.parser.type.repetition.AbstractRepetition;
import de.etgramlich.parser.type.repetition.Optional;
import de.etgramlich.parser.type.repetition.Precedence;
import de.etgramlich.parser.type.repetition.ZeroOrMore;
import de.etgramlich.parser.type.text.TextElement;
import de.etgramlich.util.exception.UnrecognizedElementException;
import de.etgramlich.util.graph.type.BnfRuleGraph;
import de.etgramlich.util.graph.type.Scope;
import de.etgramlich.util.graph.type.ScopeEdge;
import de.etgramlich.util.graph.type.node.Node;
import de.etgramlich.util.graph.type.node.SequenceNode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Builds a Graph with Scopes as vertices and Node as edges.
 * List of rules must be passed as constructor argument, getGraph can be passed on each constructed object.
 */
public final class GraphBuilder {
    private final BnfRuleGraph graph = new BnfRuleGraph();
    private int scopeNumber = 0;
    private Scope lastAddedScope;

    /**
     * Stack for beginning Scopes for alternatives.
     */
    private final Deque<Scope> openingAlternativeStack = new ArrayDeque<>();

    /**
     * Stack for last Scopes of alternatives. (add() and peek()/poll())
     */
    private final Deque<Scope> closingAlternativeStack = new ArrayDeque<>();

    /**
     * Stack saving the opening scopes of the optional elements.
     */
    private final Deque<Scope> openingOptionalStack = new ArrayDeque<>();

    /**
     * Stack saving the opening scopes of loops.
     */
    private final Deque<Scope> openingLoopStack = new ArrayDeque<>();

    public GraphBuilder(final Bnf bnf) {
        final List<BnfRule> startBnfRules = bnf.getBnfRules().stream().filter(BnfRule::isStartRule).collect(Collectors.toList());
        if (startBnfRules.size() != 1) {
            throw new IllegalArgumentException("Rule-list must have exactly one Start rule");
        }
        final BnfRule startBnfRule = startBnfRules.get(0);
        final List<BnfRule> nonTerminalBnfRules = bnf.getBnfRules().stream().filter(
                bnfRule -> !bnfRule.isTerminal() && bnfRule.getNumberOfElements() > 1
        ).collect(Collectors.toList());
        nonTerminalBnfRules.remove(startBnfRule);

        // Add first scope
        final Scope firstScope = getNextScope();
        graph.addVertex(firstScope);
        lastAddedScope = firstScope;
        // Parse all rules
        for (BnfRule bnfRule : nonTerminalBnfRules) {
            processAlternatives(bnfRule.getRhs());
        }
    }

    private void processAlternatives(final Alternatives alternatives) {
        assert (!alternatives.getSequences().isEmpty());

        openingAlternativeStack.push(lastAddedScope);
        closingAlternativeStack.add(getNextScope());

        for (Sequence sequence : alternatives.getSequences()) {
            processSequence(sequence);
            lastAddedScope = openingAlternativeStack.peek();
        }
        openingAlternativeStack.pop();

        // Replace scopes of dangling edges with only one (new) one to implement recursive alternatives
        final Set<ScopeEdge> danglingEdges = graph.getDanglingScopeEdges();
        if (danglingEdges.size() == 1) {
            lastAddedScope = graph.getEndScope();
        } else {
            final Scope targetScope = closingAlternativeStack.removeFirst();
            graph.addVertex(targetScope);
            mergeNodeTargets(targetScope, danglingEdges);
            lastAddedScope = targetScope;
        }
    }

    private void mergeNodeTargets(final Scope newScope, final Collection<ScopeEdge> edges) {
        for (ScopeEdge edge : edges) {
            // Remove old Vertex and Edge
            Scope temp = edge.getTarget();
            graph.removeVertex(temp);
            graph.removeEdge(edge);

            // Re-add altered edge
            edge.setTarget(newScope);
            graph.addEdge(edge.getSource(), newScope, edge);
        }
    }

    private void processSequence(final Sequence sequence) {
        assert (sequence.getElements().size() > 0);

        for (Element element : sequence.getElements()) {
            processElement(element);
        }
    }

    /**
     * Processes given element and adds it to the Graph.
     *
     * @param element Element of EBNF grammar to be added, must not be null.
     */
    private void processElement(final Element element) {
        if (element instanceof TextElement) {
            addNodeInSequence(new SequenceNode(element.getName()));
        } else if (element instanceof AbstractRepetition) {
            AbstractRepetition repetition = (AbstractRepetition) element;

            if (repetition instanceof Optional) {
                openingOptionalStack.push(lastAddedScope);
                processAlternatives(repetition.getAlternatives());
                graph.addEdge(openingOptionalStack.pop(), lastAddedScope, null);
            } else if (repetition instanceof ZeroOrMore) {
                openingLoopStack.push(lastAddedScope);
                processAlternatives(repetition.getAlternatives());
                graph.addEdge(lastAddedScope, openingLoopStack.pop(), null);
            } else if (repetition instanceof Precedence) {
                processAlternatives(repetition.getAlternatives());
            } else {
                throw new UnrecognizedElementException("Element is unrecognized subclass of AbstractRepetition: " + element.toString());
            }
        } else {
            throw new UnrecognizedElementException("Element not recognized: " + element.toString());
        }
    }

    public BnfRuleGraph getGraph() {
        return graph;
    }

    /**
     * Adds a node in sequence to the graph following the last added scope and followed by a newly created scope.
     *
     * @param node  Node(s) to be associated with the edge.
     */
    public void addNodeInSequence(final Node node) {
        final Scope newScope = getNextScope();
        graph.addVertex(newScope);

        graph.addEdge(lastAddedScope, newScope, new ScopeEdge(lastAddedScope, newScope, node));

        lastAddedScope = newScope;
    }

    /**
     * Get Scopes with consecutively numbered names.
     *
     * @return New Scope with individual name.
     */
    private Scope getNextScope() {
        return new Scope("Scope_" + scopeNumber++);
    }
}
