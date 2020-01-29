package de.etgramlich.util.graph;

import com.google.common.collect.Lists;
import de.etgramlich.parser.type.*;
import de.etgramlich.parser.type.repetition.Optional;
import de.etgramlich.parser.type.repetition.Precedence;
import de.etgramlich.parser.type.repetition.ZeroOrMore;
import de.etgramlich.util.StringUtil;
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

    private Node currentNode;
    private boolean isInAlternatives;
    private boolean isInAlternative;
    private boolean isInRepetition;
    private boolean isInSequence;

    private void reset() {
        isInAlternatives = false;
        isInAlternative  = false;
        isInRepetition   = false;
        isInSequence     = false;

        currentNode = null;
    }

    public GraphBuilder(@NotNull final Bnf bnf) {
        final List<BnfRule> startBnfRules = bnf.getBnfRules().stream().filter(BnfRule::isStartRule).collect(Collectors.toList());
        if (startBnfRules.size() != 1) {
            throw new IllegalArgumentException("Rule-list must have exactly one Start rule");
        }
        final BnfRule startBnfRule = startBnfRules.get(0);
        final List<BnfRule> nonTerminalBnfRules = bnf.getBnfRules().stream().filter(
                bnfRule -> !bnfRule.isTerminal() && bnfRule.getNonTerminalDependants().size() > 1
        ).collect(Collectors.toList());
        graphWrapper = new GraphWrapper(startBnfRule.getName());
        nonTerminalBnfRules.remove(startBnfRule);

        for (BnfRule bnfRule : nonTerminalBnfRules) {
            processAlternatives(bnfRule.getRhs());
        }
    }

    private void addNode(final String nextScopeName) {
        if (StringUtil.isBlank(nextScopeName)) {
            throw new IllegalArgumentException("Scope name must not be blank!");
        }
        Scope currentScope = new Scope(nextScopeName);
        if (currentNode instanceof LoopNode) {
            graphWrapper.addLoop(currentScope, currentNode);
        } else if (currentNode instanceof SequenceNode) {
            if (currentNode.isOptional())  graphWrapper.addOptional(currentScope, currentNode);  // Optional
            else                           graphWrapper.addSequence(currentScope, currentNode);  // Sequnce / one element
        } else if (currentNode instanceof AlternativeNode) {
            graphWrapper.addSequence(currentScope, currentNode);
        }
        reset();
    }

    private void processAlternatives(@NotNull final Alternatives alternatives) {
        assert (!alternatives.getSequences().isEmpty());
        if (alternatives.getSequences().size() > 1) {
            reset();
            isInAlternatives = true;
            if (currentNode == null) {
                currentNode = new AlternativeNode(alternatives.getSequences().get(0).getName());
            }
        }
        // Alternative, Sequence, Optional, ZeroOrMore, Precedence, ID or LetterRange
        for (Sequence sequence : alternatives.getSequences()) {
            processSequence(sequence);
        }
    }
    private void processSequence(@NotNull final Sequence sequence) {
        assert (sequence.getElements().size() > 0);
        if (isInAlternatives) {                             // Alternative
            ((AlternativeNode) currentNode).addAlternative(new SequenceNode(sequence.getName()));
        } else if (sequence.getElements().size() > 1) {  // Sequence
            reset();
            isInSequence = true;
            for (Element element : sequence.getElements()) {
                processElement(element);
            }
        } else {    // Optional, ZeroOrMore, Precedence, ID or LetterRange
            processElement(sequence.getElements().get(0));
        }
    }

    /**
     * Processes gíven element and adds it to the Graph.
     * @param element Element of EBNF grammar to be added, must not be null.
     */
    private void processElement(@NotNull final Element element) {
        if (isInSequence) { // Sequence
            getLastOfSequence().setSuccessor(new SequenceNode(element.getName()));
        } else if (element instanceof Optional) {
            // ToDo: Optional
        } else if (element instanceof ZeroOrMore) {
            // ToDo: Repetition
        } else if (element instanceof Precedence) {
            // ToDo: Precedence
        } else if (element instanceof NonTerminal) {
            // ToDo: NonTerminal
        } else {
            // ToDo: TextElement
            TextElement id = (TextElement) element;
            if (id.isTerminal()) {
                addNode(id.getName());
            } else {
                // ToDo: id
            }
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


    /**
     * Splits the passed list into sublist from one terminal (inclusive) to the next terminal (exclusive).
     * So each sub-list contains one terminal as the first element followed by zero or more non-terminals.
     * @param alternatives List of alternatives (RHS of a rule).
     * @return A List of List of Alternatives, not null, may be empty.
     */
    private static List<List<Sequence>> getAlternativeScopes(@NotNull final List<Sequence> alternatives) {
        final List<List<Sequence>> scopes = new ArrayList<>();

        List<Sequence> currentList = new ArrayList<>();
        for (Sequence alternative : alternatives) {
            if (alternative.isTerminal() && !currentList.isEmpty()) {
                scopes.add(currentList);
                currentList = new ArrayList<>();
            }
            currentList.add(alternative);
        }
        if (!currentList.isEmpty()) {
            scopes.add(currentList);
        }

        return scopes;
    }

    private static Node getNode(@NotNull final List<Sequence> rhs) {
        final long numTerminalNodes = rhs.stream().filter(Sequence::isTerminal).count();
        if (numTerminalNodes > 1) {
            throw new IllegalArgumentException("Must only contain one Terminal!");
        }

        if (rhs.size() - numTerminalNodes > 1) {   // Alternative
            return buildAlternative(rhs);
        } else {
            List<Element> elements = rhs.stream().filter(a -> !a.isTerminal()).findFirst().get().getElements();
            if (elements.size() > 1) {              // Sequenz
                return buildSequence(elements);
            } else {
                Element element = elements.get(0);
                if (element instanceof ZeroOrMore) {// Loop
                    return buildLoop(element);
                } else {                            // Single element
                    return new SequenceNode(element.getName(), element instanceof Optional);
                }
            }
        }
    }

    private static Node getNode(final Element element) {
        // ToDo
        return null;
    }

    private static LoopNode buildLoop(@NotNull final Element element) {
        final SequenceNode child = new SequenceNode(element.getName());
        return new LoopNode(element.getName(), child);
    }

    private static AlternativeNode buildAlternative(@NotNull final List<Sequence> rhs) {
        final String name = rhs.get(0).getName();
        final List<Element> elements =
                rhs.stream().filter(a->!a.isTerminal()).map(alternative -> alternative.getElements().get(0))
                        .collect(Collectors.toList());
        final List<SequenceNode> alternatives =
                elements.stream().map(element -> new SequenceNode(element.getName())).collect(Collectors.toList());
        return new AlternativeNode(name, alternatives);
    }

    private static SequenceNode buildSequence(final List<Element> elements) {
        SequenceNode begin = null;
        for (Element element : Lists.reverse(elements)) {
            begin = new SequenceNode(element.getName(), begin);
        }
        return begin;
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
