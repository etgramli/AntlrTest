package de.etgramlich.util.graph;

import de.etgramlich.parser.type.*;
import de.etgramlich.parser.type.repetition.AbstractRepetition;
import de.etgramlich.parser.type.repetition.Optional;
import de.etgramlich.parser.type.repetition.Precedence;
import de.etgramlich.parser.type.repetition.ZeroOrMore;
import de.etgramlich.parser.type.text.TextElement;
import de.etgramlich.util.exception.InvalidGraphException;
import de.etgramlich.util.exception.UnrecognizedElementException;
import de.etgramlich.util.graph.type.*;
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

    public GraphBuilder(final Bnf bnf) {
        final List<BnfRule> startBnfRules = bnf.getBnfRules().stream()
                .filter(BnfRule::isStartRule)
                .collect(Collectors.toList());
        if (startBnfRules.size() != 1) {
            throw new IllegalArgumentException("Rule-list must have exactly one Start rule");
        }
        final BnfRule startBnfRule = startBnfRules.get(0);
        final List<BnfRule> nonTerminalBnfRules = bnf.getBnfRules().stream()
                .filter(bnfRule -> !bnfRule.isTerminal() && bnfRule.getNumberOfElements() > 1)
                .collect(Collectors.toList());
        nonTerminalBnfRules.remove(startBnfRule);

        // Add first scope
        final Scope firstScope = getNextScope();
        graph.addVertex(firstScope);
        lastAddedScope = firstScope;
        // Parse all rules
        for (BnfRule bnfRule : nonTerminalBnfRules) {
            processAlternatives(bnfRule.getRhs());
        }

        if (!graph.isConsistent()) {
            throw new InvalidGraphException("Graph is not consistent after build!");
        }
    }

    private void processAlternatives(final Alternatives alternatives) {
        assert (!alternatives.getSequences().isEmpty());

        final Scope openingAlternativeScope = lastAddedScope;
        final Scope closingAlternativeScope = getNextScope();
        final List<Scope> lastScopes = new ArrayList<>();

        for (int i = 0; i < alternatives.getSequences().size(); ++i) {
            processSequence(alternatives.getSequences().get(i));
            lastScopes.add(lastAddedScope);
            if (i != alternatives.getSequences().size() - 1) {
                lastAddedScope = openingAlternativeScope;
            }
        }

        // Replace scopes of dangling edges with only one (new) one to implement recursive alternatives
        if (lastScopes.size() > 1) {
            graph.addVertex(closingAlternativeScope);
            mergeNodes(closingAlternativeScope, lastScopes);
            lastAddedScope = closingAlternativeScope;
        }
    }


    private void mergeNodes(final Scope newScope, final Collection<Scope> scopes) {
        final Set<ScopeEdge> ingoingEdges = new HashSet<>();
        final Set<ScopeEdge> outgoingEdges = new HashSet<>();
        for (Scope scope : scopes) {
            ingoingEdges.addAll(graph.incomingEdgesOf(scope));
            outgoingEdges.addAll(graph.outgoingEdgesOf(scope));
        }
        // Remove vertices and touching edges
        graph.removeAllVertices(scopes);

        // Remove old target vertex and edge, re-add edge with updated vertex
        for (ScopeEdge edge : ingoingEdges) {
            edge.setTarget(newScope);
            graph.addEdge(edge.getSource(), newScope, edge);
        }

        // Remove old source vertex and edge, re-add edge with updated vertex
        for (ScopeEdge edge : outgoingEdges) {
            edge.setSource(newScope);
            graph.addEdge(newScope, edge.getTarget(), edge);
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
            final Scope beforeOptionalLoop = lastAddedScope;

            if (repetition instanceof Optional) {
                processAlternatives(repetition.getAlternatives());
                graph.addEdge(beforeOptionalLoop, lastAddedScope, new OptionalEdge(beforeOptionalLoop, lastAddedScope));
            } else if (repetition instanceof ZeroOrMore) {
                processAlternatives(repetition.getAlternatives());
                graph.addEdge(lastAddedScope, beforeOptionalLoop, new RepetitionEdge(lastAddedScope, beforeOptionalLoop));
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
        return (BnfRuleGraph) graph.clone();
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
