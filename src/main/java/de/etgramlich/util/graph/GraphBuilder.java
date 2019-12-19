package de.etgramlich.util.graph;

import com.google.common.collect.Lists;
import de.etgramlich.parser.type.Rule;
import de.etgramlich.parser.type.RuleList;
import de.etgramlich.parser.type.rhstype.Alternative;
import de.etgramlich.parser.type.rhstype.Element;
import de.etgramlich.parser.type.terminal.AbstractId;
import de.etgramlich.util.StringUtil;
import de.etgramlich.util.graph.type.GraphWrapper;
import de.etgramlich.util.graph.type.Scope;
import de.etgramlich.util.graph.type.ScopeEdge;
import de.etgramlich.util.graph.type.node.AlternativeNode;
import de.etgramlich.util.graph.type.node.LoopNode;
import de.etgramlich.util.graph.type.node.Node;
import de.etgramlich.util.graph.type.node.SequenceNode;
import org.jetbrains.annotations.Contract;
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

    public GraphBuilder(@NotNull final RuleList ruleList) {
        final List<Rule> startRules = ruleList.getRules().stream().filter(Rule::isStartRule).collect(Collectors.toList());
        if (startRules.size() == 0) {
            throw new IllegalArgumentException("Rule-list must have exactly one Start rule");
        }
        final Rule startRule = startRules.get(0);
        final List<Rule> nonTerminalRules = ruleList.getRules().stream().filter(
                rule -> !rule.isTerminal() && rule.getNonTerminalDependants().size() > 1
        ).collect(Collectors.toList());
        graphWrapper = new GraphWrapper(startRule.getName());
        nonTerminalRules.remove(startRule);

        for (Rule rule : nonTerminalRules) {
            processAlternatives(rule.getRhs());
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

    private void processAlternatives(@NotNull final List<Alternative> alternatives) {
        assert (!alternatives.isEmpty());
        if (alternatives.size() > 1) {
            reset();
            isInAlternatives = true;
            if (currentNode == null) {
                currentNode = new AlternativeNode(alternatives.get(0).getName());
            }
        }
        // Alternative, Sequence, Optional, ZeroOrMore, Precedence, ID or LetterRange
        for (Alternative alternative : alternatives) {
            processAlternative(alternative);
        }
    }
    private void processAlternative(@NotNull final Alternative alternative) {
        assert (alternative.getElements().size() > 0);
        if (isInAlternatives) {                             // Alternative
            ((AlternativeNode) currentNode).addAlternative(new SequenceNode(alternative.getName()));
        } else if (alternative.getElements().size() > 1) {  // Sequence
            reset();
            isInSequence = true;
            for (Element element : alternative.getElements()) {
                processElement(element);
            }
        } else {    // Optional, ZeroOrMore, Precedence, ID or LetterRange
            processElement(alternative.getElements().get(0));
        }
    }

    private void processElement(@NotNull final Element element) {
        if (isInSequence) { // Sequence
            getLastOfSequence().setSuccessor(new SequenceNode(element.getName()));
        } else if (element.isOptional()) {
            // ToDo: Optional
        } else if (element.isRepetition()) {
            // ToDo: Repetition
        } else if (element.isPrecedence()) {
            // ToDo: Precedence
        } else if (element.isLetterRange()) {
            // ToDo: LetterRange
        } else {
            // ToDo: Id
            final AbstractId id = element.getId();
            if (id.isTerminal()) {
                addNode(id.getName());
            } else {
                // ToDo: id
            }
        }
    }

    private Node getLastOfSequence() {
        if (currentNode == null) {
            return null;
        }
        Node current = currentNode;
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
    @NotNull
    private static List<List<Alternative>> getAlternativeScopes(@NotNull final List<Alternative> alternatives) {
        final List<List<Alternative>> scopes = new ArrayList<>();

        List<Alternative> currentList = new ArrayList<>();
        for (Alternative alternative : alternatives) {
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

    private static Node getNode(@NotNull final List<Alternative> rhs) {
        final long numTerminalNodes = rhs.stream().filter(Alternative::isTerminal).count();
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
                if (element.isRepetition()) {       // Loop
                    return buildLoop(element);
                } else {                            // Single element
                    return new SequenceNode(element.getName(), element.isOptional());
                }
            }
        }
    }

    private static Node getNode(final Element element) {
        // ToDo
        return null;
    }

    @NotNull
    private static LoopNode buildLoop(@NotNull final Element element) {
        SequenceNode child = new SequenceNode(element.getName());
        return new LoopNode(element.getName(), child);
    }

    @NotNull
    private static AlternativeNode buildAlternative(@NotNull final List<Alternative> rhs) {
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
    @NotNull
    @Contract(pure = true)
    public Graph<Scope, ScopeEdge> getGraph() {
        return graphWrapper.getGraph();
    }
}
