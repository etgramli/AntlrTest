package de.etgramlich.dsl.graph;

import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import de.etgramlich.dsl.graph.type.NodeEdge;
import de.etgramlich.dsl.graph.type.Scope;
import de.etgramlich.dsl.graph.type.ScopeEdge;
import de.etgramlich.dsl.parser.type.Alternatives;
import de.etgramlich.dsl.parser.type.Bnf;
import de.etgramlich.dsl.parser.type.BnfRule;
import de.etgramlich.dsl.parser.type.Sequence;
import de.etgramlich.dsl.parser.type.repetition.Precedence;
import de.etgramlich.dsl.parser.type.repetition.ZeroOrMore;
import de.etgramlich.dsl.parser.type.text.Keyword;
import de.etgramlich.dsl.parser.type.text.NonTerminal;
import de.etgramlich.dsl.parser.type.text.TextElement;
import de.etgramlich.dsl.parser.type.text.Type;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

class ForestBuilderTest {
    private static final BnfRule START_RULE;

    private static final List<NonTerminal> NON_TERMINALS;
    private static final List<Keyword> KEYWORDS;
    private static final int NUM_NON_TERMINALS = 8;
    private static final int NUM_KEYWORDS = 7;
    static {
        final List<NonTerminal> nts = new ArrayList<>(NUM_NON_TERMINALS);
        for (int i = 0; i < NUM_NON_TERMINALS; ++i) {
            nts.add(new NonTerminal("ID_" + i));
        }
        NON_TERMINALS = Collections.unmodifiableList(nts);

        final List<Keyword> keywords = new ArrayList<>(NUM_KEYWORDS);
        for (int i = 0; i < NUM_KEYWORDS; ++i) {
            keywords.add(new Keyword("Key_" + i));
        }
        KEYWORDS = Collections.unmodifiableList(keywords);

        START_RULE = new BnfRule(
                new NonTerminal("EntryPoint"),
                new Alternatives(List.of(new Sequence(List.of(NON_TERMINALS.get(0))))));
    }

    @Test
    void getMergedGraph_noNonTerminals_graphStaysSame() {
        final List<String> expected = List.of(KEYWORDS.get(0), KEYWORDS.get(1), KEYWORDS.get(2), KEYWORDS.get(3), KEYWORDS.get(4)).stream()
                .map(TextElement::getName)
                .collect(Collectors.toUnmodifiableList());
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(NON_TERMINALS.get(0),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        KEYWORDS.get(0),
                                        KEYWORDS.get(1),
                                        KEYWORDS.get(2),
                                        KEYWORDS.get(3),
                                        KEYWORDS.get(4))
                        ))))));
        final BnfRuleGraph graph = new ForestBuilder(alternativesOneNodeEach).getMergedGraph();

        assertTrue(graph.isConsistent());
        assertFalse(graph.containsNonTerminals());

        final List<GraphPath<Scope, ScopeEdge>> paths = new AllDirectedPaths<>(graph).getAllPaths(graph.getStartScope(), graph.getEndScope(), true, null);
        assertEquals(1, paths.size());
        final List<String> actual = paths.get(0).getEdgeList().stream()
                .filter(edge -> edge instanceof NodeEdge)
                .map(edge -> (NodeEdge) edge)
                .map(edge -> edge.getNode().getName())
                .collect(Collectors.toUnmodifiableList());
        assertEquals(expected, actual);
    }

    @Test
    void getMergedGraph_oneNonTerminal_oneNonTerminalGetsReplaced() {
        final List<String> expected = List.of(KEYWORDS.get(0), KEYWORDS.get(1), KEYWORDS.get(2), KEYWORDS.get(3), KEYWORDS.get(4)).stream()
                .map(TextElement::getName)
                .collect(Collectors.toUnmodifiableList());
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(NON_TERMINALS.get(0),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        KEYWORDS.get(0),
                                        NON_TERMINALS.get(1),
                                        KEYWORDS.get(2),
                                        KEYWORDS.get(3),
                                        KEYWORDS.get(4))
                                )))),
                new BnfRule(NON_TERMINALS.get(1), new Alternatives(List.of(new Sequence(List.of(KEYWORDS.get(1))))))
                ));
        final BnfRuleGraph graph = new ForestBuilder(alternativesOneNodeEach).getMergedGraph();

        assertTrue(graph.isConsistent());
        assertFalse(graph.containsNonTerminals());
        assertEquals(5, graph.length());
        assertEquals(5, graph.edgeSet().size());
        assertEquals(6, graph.vertexSet().size());

        final List<GraphPath<Scope, ScopeEdge>> paths = new AllDirectedPaths<>(graph).getAllPaths(graph.getStartScope(), graph.getEndScope(), true, null);
        assertEquals(1, paths.size());
        final List<String> actual = paths.get(0).getEdgeList().stream()
                .filter(edge -> edge instanceof NodeEdge)
                .map(edge -> (NodeEdge) edge)
                .map(edge -> edge.getNode().getName())
                .collect(Collectors.toUnmodifiableList());
        assertEquals(expected, actual);
    }

    @Test
    void getMergedGraph_oneNonTerminalMultipleTimes_oneNonTerminalGetsReplacedAllTimes() {
        final List<String> expected = List.of(KEYWORDS.get(0), KEYWORDS.get(1), KEYWORDS.get(2), KEYWORDS.get(1), KEYWORDS.get(4)).stream()
                .map(TextElement::getName)
                .collect(Collectors.toUnmodifiableList());
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(NON_TERMINALS.get(0),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        KEYWORDS.get(0),
                                        NON_TERMINALS.get(1),
                                        KEYWORDS.get(2),
                                        NON_TERMINALS.get(1),
                                        KEYWORDS.get(4))
                                )))),
                new BnfRule(NON_TERMINALS.get(1), new Alternatives(List.of(new Sequence(List.of(KEYWORDS.get(1))))))
        ));
        final BnfRuleGraph graph = new ForestBuilder(alternativesOneNodeEach).getMergedGraph();

        assertTrue(graph.isConsistent());
        assertFalse(graph.containsNonTerminals());
        assertEquals(5, graph.length());
        assertEquals(5, graph.edgeSet().size());
        assertEquals(6, graph.vertexSet().size());

        final List<GraphPath<Scope, ScopeEdge>> paths = new AllDirectedPaths<>(graph).getAllPaths(graph.getStartScope(), graph.getEndScope(), true, null);
        assertEquals(1, paths.size());
        final List<String> actual = paths.get(0).getEdgeList().stream()
                .filter(edge -> edge instanceof NodeEdge)
                .map(edge -> (NodeEdge) edge)
                .map(edge -> edge.getNode().getName())
                .collect(Collectors.toUnmodifiableList());
        assertEquals(expected, actual);
    }

    @Test
    void getMergedGraph_multipleNonTerminals_allNonTerminalGetsReplaced() {
        final List<String> expected = List.of(KEYWORDS.get(0), KEYWORDS.get(1), KEYWORDS.get(2), KEYWORDS.get(1), KEYWORDS.get(4)).stream()
                .map(TextElement::getName)
                .collect(Collectors.toUnmodifiableList());
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(NON_TERMINALS.get(0),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        KEYWORDS.get(0),
                                        NON_TERMINALS.get(1),
                                        NON_TERMINALS.get(2),
                                        NON_TERMINALS.get(1),
                                        KEYWORDS.get(4)))))
                ),
                new BnfRule(NON_TERMINALS.get(1), new Alternatives(List.of(new Sequence(List.of(KEYWORDS.get(1)))))),
                new BnfRule(NON_TERMINALS.get(2), new Alternatives(List.of(new Sequence(List.of(KEYWORDS.get(2))))))
        ));
        final BnfRuleGraph graph = new ForestBuilder(alternativesOneNodeEach).getMergedGraph();

        assertTrue(graph.isConsistent());
        assertFalse(graph.containsNonTerminals());
        assertEquals(5, graph.length());
        assertEquals(5, graph.edgeSet().size());
        assertEquals(6, graph.vertexSet().size());

        final List<GraphPath<Scope, ScopeEdge>> paths = new AllDirectedPaths<>(graph).getAllPaths(graph.getStartScope(), graph.getEndScope(), true, null);
        assertEquals(1, paths.size());
        final List<String> actual = paths.get(0).getEdgeList().stream()
                .filter(edge -> edge instanceof NodeEdge)
                .map(edge -> (NodeEdge) edge)
                .map(edge -> edge.getNode().getName())
                .collect(Collectors.toUnmodifiableList());
        assertEquals(expected, actual);
    }

    @Test
    void getMergedGraph_multipleRecursiveNonTerminals_allNonTerminalGetsReplaced() {
        final List<String> expected = List.of(
                KEYWORDS.get(0), KEYWORDS.get(5), KEYWORDS.get(6), KEYWORDS.get(2), KEYWORDS.get(5), KEYWORDS.get(6), KEYWORDS.get(4))
                .stream().map(TextElement::getName).collect(Collectors.toUnmodifiableList());
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(NON_TERMINALS.get(0),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        KEYWORDS.get(0),
                                        NON_TERMINALS.get(1),
                                        NON_TERMINALS.get(2),
                                        NON_TERMINALS.get(1),
                                        KEYWORDS.get(4))
                                )))),
                new BnfRule(NON_TERMINALS.get(1), new Alternatives(List.of(new Sequence(List.of(KEYWORDS.get(5), NON_TERMINALS.get(3)))))),
                new BnfRule(NON_TERMINALS.get(2), new Alternatives(List.of(new Sequence(List.of(KEYWORDS.get(2)))))),
                new BnfRule(NON_TERMINALS.get(3), new Alternatives(List.of(new Sequence(List.of(KEYWORDS.get(6))))))
        ));
        final BnfRuleGraph graph = new ForestBuilder(alternativesOneNodeEach).getMergedGraph();

        assertTrue(graph.isConsistent());
        assertFalse(graph.containsNonTerminals());
        assertEquals(7, graph.length());
        assertEquals(7, graph.edgeSet().size());
        assertEquals(8, graph.vertexSet().size());

        final List<GraphPath<Scope, ScopeEdge>> paths =
                new AllDirectedPaths<>(graph).getAllPaths(graph.getStartScope(), graph.getEndScope(), true, null);
        assertEquals(1, paths.size());
        final List<String> actual = paths.get(0).getEdgeList().stream()
                .filter(edge -> edge instanceof NodeEdge)
                .map(edge -> ((NodeEdge) edge).getNode().getName())
                .collect(Collectors.toUnmodifiableList());
        assertEquals(expected, actual);
    }

    @Test
    void getMergedGraph_joi() {
        final Bnf joi = new Bnf(List.of(
            new BnfRule(new NonTerminal("joi"),
                new Alternatives(List.of(new Sequence(List.of(new NonTerminal("component")))))),
            new BnfRule(new NonTerminal("component"),
                new Alternatives(List.of(
                        new Sequence(List.of(
                                new Precedence(new Alternatives(List.of(
                                        new Sequence(List.of(new Keyword("'component'"))),
                                        new Sequence(List.of(new Keyword("'singleton'")))))),
                                new NonTerminal("componentName"),
                                new NonTerminal("componentInterface"),
                                new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(
                                        new NonTerminal("componentInterface")))))),
                                new NonTerminal("componentMethod"),
                                new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(
                                        new NonTerminal("componentMethod")))))),
                                new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(
                                        new NonTerminal("componentField"))))))
                        ))))),
            new BnfRule(
                new NonTerminal("componentName"),
                new Alternatives(List.of(new Sequence(List.of(new Type("String")))))),
            new BnfRule(
                new NonTerminal("componentInterface"),
                new Alternatives(List.of(
                        new Sequence(List.of(new Keyword("impl"), new Type("String")))))),
            new BnfRule(
                new NonTerminal("componentMethod"),
                new Alternatives(List.of(
                        new Sequence(List.of(new Keyword("method"), new Type("String")))))),
            new BnfRule(
                new NonTerminal("componentField"),
                new Alternatives(List.of(
                        new Sequence(List.of(new Keyword("field"), new Type("String"))))))));

        final BnfRuleGraph graph = new ForestBuilder(joi).getMergedGraph();

        assertFalse(graph.containsNonTerminals());
        assertTrue(graph.isConsistent());
        assertEquals(15, graph.vertexSet().size());
        assertEquals(18, graph.edgeSet().size());
        assertEquals(11, graph.length());
    }

    @Test
    void getMergedGraph_circularDependency_throwsException() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(NON_TERMINALS.get(0),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        KEYWORDS.get(0),
                                        NON_TERMINALS.get(1),
                                        KEYWORDS.get(2),
                                        KEYWORDS.get(3),
                                        KEYWORDS.get(4))
                                )))),
                new BnfRule(NON_TERMINALS.get(1), new Alternatives(List.of(new Sequence(List.of(NON_TERMINALS.get(1))))))
        ));
        assertThrows(IllegalArgumentException.class, () -> new ForestBuilder(alternativesOneNodeEach));
    }
}
