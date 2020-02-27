package de.etgramlich.graph;

import de.etgramlich.parser.type.Bnf;
import de.etgramlich.parser.type.BnfRule;
import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.Sequence;
import de.etgramlich.parser.type.Element;
import de.etgramlich.parser.type.repetition.AbstractRepetition;
import de.etgramlich.parser.type.repetition.Optional;
import de.etgramlich.parser.type.repetition.ZeroOrMore;
import de.etgramlich.parser.type.text.TextElement;
import de.etgramlich.util.exception.InvalidGraphException;
import de.etgramlich.util.exception.UnrecognizedElementException;
import de.etgramlich.graph.type.BnfRuleGraph;
import de.etgramlich.graph.type.Scope;
import de.etgramlich.graph.type.ScopeEdge;
import de.etgramlich.graph.type.NodeEdge;
import de.etgramlich.graph.type.OptionalEdge;
import de.etgramlich.graph.type.RepetitionEdge;
import de.etgramlich.graph.type.Node;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Builds a Graph with Scopes as vertices and Node as edges.
 * List of rules must be passed as constructor argument, getGraph can be passed on each constructed object.
 */
public final class GraphBuilder {
    /**
     * Graph representing the bnf passed in constructor.
     * Can be accessed if constructor succeeds.
     */
    private final BnfRuleGraph graph = new BnfRuleGraph();

    /**
     * Counter to create Scopes with unique names.
     */
    private int scopeNumber = 0;

    /**
     * Saves the last added scope.
     */
    private Scope lastAddedScope;

    /**
     * Creates a BnfRuleGraph from a Bnf (tree).
     * @param bnf A bnf tree, must not be null.
     */
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
        final Set<Scope> lastScopes = new HashSet<>();

        for (Iterator<Sequence> iter = alternatives.getSequences().iterator(); iter.hasNext();) {
            processSequence(iter.next());
            lastScopes.add(lastAddedScope);
            if (iter.hasNext()) {
                lastAddedScope = openingAlternativeScope;
            }
        }

        // Replace scopes of dangling edges with only one (new) one to implement recursive alternatives
        if (lastScopes.size() > 1) {
            final Scope closingAlternativeScope = getNextScope();
            graph.addVertex(closingAlternativeScope);
            mergeNodes(closingAlternativeScope, lastScopes);
            lastAddedScope = closingAlternativeScope;
        }
    }

    /**
     * Replaces the scopes by the new scope, to merge edges to a single target or source vertex.
     * @param newScope New scope, must not be null.
     * @param scopes Collection with scopes to replace.
     */
    private void mergeNodes(final Scope newScope, final Collection<Scope> scopes) {
        if (newScope == null || scopes == null) {
            throw new IllegalArgumentException("New scope and scopes must not be null!");
        }
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
            graph.addEdge(edge.getSource(), newScope, edge);
        }

        // Remove old source vertex and edge, re-add edge with updated vertex
        for (ScopeEdge edge : outgoingEdges) {
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
            addNodeInSequence(new Node(element.getName()));
        } else if (element instanceof AbstractRepetition) {
            AbstractRepetition repetition = (AbstractRepetition) element;

            final Scope beforeOptionalLoop = lastAddedScope;
            processAlternatives(repetition.getAlternatives());

            if (repetition instanceof Optional || repetition instanceof ZeroOrMore) {
                graph.addEdge(beforeOptionalLoop, lastAddedScope, new OptionalEdge());
                if (repetition instanceof ZeroOrMore) {
                    graph.addEdge(lastAddedScope, beforeOptionalLoop, new RepetitionEdge());
                }
            }
        } else {
            throw new UnrecognizedElementException("Element not recognized: " + element.toString());
        }
    }

    /**
     * Returns a copy of the graph.
     * @return BnfRuleGraph, not null.
     */
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
        graph.addEdge(lastAddedScope, newScope, new NodeEdge(node));
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
