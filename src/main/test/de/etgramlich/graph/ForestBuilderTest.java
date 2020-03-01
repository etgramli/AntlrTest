package de.etgramlich.graph;

import de.etgramlich.graph.type.BnfRuleGraph;
import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.Bnf;
import de.etgramlich.parser.type.BnfRule;
import de.etgramlich.parser.type.Sequence;
import de.etgramlich.parser.type.text.Keyword;
import de.etgramlich.parser.type.text.NonTerminal;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

class ForestBuilderTest {
    private static final BnfRule START_RULE;

    private static final List<NonTerminal> NON_TERMINALS;
    private static final List<Keyword> KEYWORDS;
    private static final int NUM_NON_TERMINALS = 8;
    private static final int NUM_KEYWORDS = 7;
    static {
        List<NonTerminal> nts = new ArrayList<>(NUM_NON_TERMINALS);
        for (int i = 0; i < NUM_NON_TERMINALS; ++i) {
            nts.add(new NonTerminal("ID_" + i));
        }
        NON_TERMINALS = Collections.unmodifiableList(nts);

        List<Keyword> keywords = new ArrayList<>(NUM_KEYWORDS);
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
    }

    @Test
    void getMergedGraph_oneNonTerminal_oneNonTerminalGetsReplaced() {
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
    }

    @Test
    void getMergedGraph_oneNonTerminalMultipleTimes_oneNonTerminalGetsReplacedAllTimes() {
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
    }

    @Test
    void getMergedGraph_multipleNonTerminals_allNonTerminalGetsReplaced() {
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
                new BnfRule(NON_TERMINALS.get(1), new Alternatives(List.of(new Sequence(List.of(KEYWORDS.get(1)))))),
                new BnfRule(NON_TERMINALS.get(2), new Alternatives(List.of(new Sequence(List.of(KEYWORDS.get(2))))))
        ));
        final BnfRuleGraph graph = new ForestBuilder(alternativesOneNodeEach).getMergedGraph();

        assertTrue(graph.isConsistent());
        assertFalse(graph.containsNonTerminals());
        assertEquals(5, graph.length());
        assertEquals(5, graph.edgeSet().size());
        assertEquals(6, graph.vertexSet().size());
    }

    @Test
    void getMergedGraph_multipleRecursiveNonTerminals_allNonTerminalGetsReplaced() {
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
    }

    @Test
    void getMergedGraph_curcularDependency_throwsException() {
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
