package de.etgramlich.antlr.util.graph;

import com.google.common.collect.Lists;
import de.etgramlich.antlr.parser.type.Rule;
import de.etgramlich.antlr.parser.type.RuleList;
import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.rhstype.Element;
import de.etgramlich.antlr.util.graph.type.GraphWrapper;
import de.etgramlich.antlr.util.graph.type.Scope;
import de.etgramlich.antlr.util.graph.type.ScopeEdge;
import de.etgramlich.antlr.util.graph.type.node.AlternativeNode;
import de.etgramlich.antlr.util.graph.type.node.Node;
import de.etgramlich.antlr.util.graph.type.node.SequenceNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a Graph with Scopes as vertices and Node as edges.
 */
public final class GraphBuilder {

    private final GraphWrapper graphWrapper;

    public GraphBuilder(@NotNull final RuleList ruleList) {
        graphWrapper = new GraphWrapper();

        final List<Rule> nonTerminalRules = ruleList.getRules().stream().filter(rule -> !rule.isTerminal()).collect(Collectors.toList());
        nonTerminalRules.forEach(rule -> addAlternatives(rule.getRhs()));
    }


    /**
     * Splits the RHS of the rule on every terminal and adds the edges for each terminal.
     * @param rule Rule to process its RHS.
     */
    public void addRuleToGraph(@NotNull final Rule rule) {
        final List<Node> ruleScopes =
                getAlternativeScopes(rule.getRhs()).stream().map(this::getNode).collect(Collectors.toList());
        for (Node node : ruleScopes) {
            // ToDo
            graphWrapper.addSequence(new Scope(rule.getName()), node);
        }
    }

    private List<Node> getRuleNodes(@NotNull final Rule rule) {
        return getAlternativeScopes(rule.getRhs()).stream().map(this::getNode).collect(Collectors.toList());
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
    private Node getNode(@NotNull final List<Alternative> alternatives) {
        if (alternatives.stream().filter(Alternative::isTerminal).count() > 1) {
            throw new IllegalArgumentException("Must only contain one Terminal!");
        }
        // ToDo 

        return null;
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

    /**
     * Adds a Scope with alternatives to the graph.
     * @param alternatives Alternatives list, must not be null and not empty.
     */
    private void addAlternatives(@NotNull final List<Alternative> alternatives) {
        if (alternatives.isEmpty()) {
            throw new IllegalArgumentException("Alternatives is empty!!");
        }
        final Scope scope = new Scope(alternatives.get(0).getName());
        final List<SequenceNode> nodeList = alternatives.stream().map(this::getAlternativeSequence).collect(Collectors.toList());
        final AlternativeNode node = new AlternativeNode(alternatives.get(0).getName(), nodeList);
        graphWrapper.addSequence(scope, node);
    }

    /**
     * Get alternatives contained in one AlternativeNode
     * @param alternatives List of alternatives, must not be null.
     * @return An AlternativeNode, never null.
     */
    @NotNull
    private AlternativeNode getAlternatives(@NotNull final List<Alternative> alternatives) {
        final List<SequenceNode> alternativesNodes =
                alternatives.stream().map(this::getAlternativeSequence).collect(Collectors.toList());
        return new AlternativeNode(alternatives.get(0).getName(), alternativesNodes);
    }

    /**
     * Returns a sequence of Sequence of nodes for the elements of the given Alternative (bnf).
     * If there is only one element in the alternative, the first SequenceNode's successor is null.
     * @param alternative Non-Null Alternative with at least 1 element.
     * @return SequenceNode, not null.
     */
    private SequenceNode getAlternativeSequence(@NotNull final Alternative alternative) {
        if (alternative.getElements().size() == 0) {
            throw new IllegalArgumentException("Alternatives with no elements!");
        }

        SequenceNode alternativeNode = null;
        for (Element element : Lists.reverse(alternative.getElements())) {
            alternativeNode = new SequenceNode(element.getName(), alternativeNode);
        }

        return alternativeNode;
    }

    private void addElement(@NotNull final Element element) {
        final Scope scope = new Scope(element.getName());
        final Node elementNode = getElement(element);

        if (element.isOptional()) {
            graphWrapper.addOptional(scope, elementNode);
        } else if (element.isRepetition()) {
            graphWrapper.addLoop(scope, elementNode);
        } else if(element.isPrecedence() || element.isId()) {
            graphWrapper.addSequence(scope, elementNode);
        } else {
            throw new UnsupportedOperationException("Unknown element type!");
        }
    }

    @NotNull
    private Node getElement(@NotNull final Element element) {
        if (element.isId()) {
            return new SequenceNode(element.getId().getText());
        } else if (element.isRepetition()) {
            return getAlternatives(element.getRepetition());
        } else {// Is LetterRange
            return new SequenceNode(element.getName());
        }
    }
}
