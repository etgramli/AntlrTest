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
import de.etgramlich.antlr.util.graph.type.node.LoopNode;
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
 * List of rules must be passed as constructor argument, getGraph can be passed on each constructed object.
 */
public final class GraphBuilder {
    private final GraphWrapper graphWrapper = new GraphWrapper();

    public GraphBuilder(@NotNull final RuleList ruleList) {
        final List<Rule> nonTerminalRules = ruleList.getRules().stream().filter(rule -> !rule.isTerminal()).collect(Collectors.toList());

        for (Rule rule : nonTerminalRules) {
            for (List<Alternative> alternativeScope : getAlternativeScopes(rule.getRhs())) {
                Scope scope = new Scope(alternativeScope.get(0).getName());
                Node node = getNode(alternativeScope);

                if (node instanceof LoopNode) {
                    graphWrapper.addLoop(scope, node);
                } else if (node instanceof SequenceNode) {
                    if (node.isOptional())  graphWrapper.addOptional(scope, node);  // Optional
                    else                    graphWrapper.addSequence(scope, node);  // Sequnce / one element
                } else if (node instanceof AlternativeNode) {
                    graphWrapper.addSequence(scope, node);
                }
            }
        }
    }

    private static List<Node> getRuleNodes(@NotNull final Rule rule) {
        return getAlternativeScopes(rule.getRhs()).stream().map(GraphBuilder::getNode).collect(Collectors.toList());
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
