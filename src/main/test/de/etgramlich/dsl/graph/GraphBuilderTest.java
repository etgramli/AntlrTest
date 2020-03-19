package de.etgramlich.dsl.graph;

import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import de.etgramlich.dsl.graph.type.Scope;
import de.etgramlich.dsl.graph.type.ScopeEdge;
import de.etgramlich.dsl.parser.type.Alternatives;
import de.etgramlich.dsl.parser.type.Bnf;
import de.etgramlich.dsl.parser.type.BnfRule;
import de.etgramlich.dsl.parser.type.repetition.Optional;
import de.etgramlich.dsl.parser.type.repetition.Precedence;
import de.etgramlich.dsl.parser.type.repetition.ZeroOrMore;
import de.etgramlich.dsl.parser.type.text.Keyword;
import de.etgramlich.dsl.parser.type.text.NonTerminal;
import de.etgramlich.dsl.parser.type.Sequence;
import de.etgramlich.dsl.parser.type.text.TextElement;
import org.jgrapht.alg.cycle.CycleDetector;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

class GraphBuilderTest {
    private static final BnfRule START_RULE = new BnfRule(
            new NonTerminal("EntryPoint"),
            new Alternatives(List.of(new Sequence(List.of(new NonTerminal("FirstRule"))))));

    private static final List<NonTerminal> NON_TERMINALS;
    private static final List<Keyword> KEYWORDS;
    private static final int NUM_NON_TERMINALS = 8;
    private static final int NUM_KEYWORDS = 5;
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
    }

    @Test
    void graphBuilder_oneRule_joi() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("component"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        new Precedence(new Alternatives(List.of(
                                                new Sequence(List.of(new Keyword("'component'"))),
                                                new Sequence(List.of(new Keyword("'singleton'")))))),
                                        new NonTerminal("componentName"),
                                        new NonTerminal("componentInterface"),
                                        new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(new NonTerminal("componentInterface")))))),
                                        new NonTerminal("componentMethod"),
                                        new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(new NonTerminal("componentMethod")))))),
                                        new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(new NonTerminal("componentField"))))))
                                ))
                        )))));
        final BnfRuleGraph graph = new GraphBuilder(alternativesOneNodeEach).getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(7, graph.length());
        assertEquals(8, graph.vertexSet().size());
        assertEquals(14, graph.edgeSet().size());
    }

    @Test
    void graphBuilder_oneRule_loopInAlternative() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Loop"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        NON_TERMINALS.get(0),
                                        new Precedence(new Alternatives(List.of(
                                                new Sequence(List.of(NON_TERMINALS.get(1))),
                                                new Sequence(List.of(new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(NON_TERMINALS.get(2)))))))),
                                                new Sequence(List.of(NON_TERMINALS.get(3)))))),
                                        NON_TERMINALS.get(4)))
                        )))));
        final BnfRuleGraph graph = new GraphBuilder(alternativesOneNodeEach).getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(4, graph.vertexSet().size());
        assertEquals(7, graph.edgeSet().size());

        final Scope startScope = graph.getStartScope();
        final Scope endScope = graph.getEndScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        final Scope secondScope = graph.getSuccessors(startScope).iterator().next();
        assertEquals(1, graph.inDegreeOf(secondScope));
        assertEquals(4, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(endScope).size());
        final Scope secondLastScope = graph.getPredecessors(endScope).iterator().next();
        assertEquals(2, graph.outDegreeOf(secondLastScope));
        assertEquals(5, graph.inDegreeOf(secondLastScope));

        final CycleDetector<Scope, ScopeEdge> cycleDetector = new CycleDetector<>(graph);
        assertTrue(cycleDetector.detectCycles());
        assertEquals(Set.of(secondLastScope), cycleDetector.findCycles());
        assertEquals(Set.of(endScope), graph.getSuccessors(secondLastScope));
        assertEquals(Set.of(endScope), graph.getOutGoingScopes(secondLastScope));
        assertEquals(Set.of(secondLastScope), graph.getSuccessors(secondScope));
    }

    @Test
    void graphBuilder_oneRule_alternativeInLoop() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Loop"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        NON_TERMINALS.get(0),
                                        new ZeroOrMore(new Alternatives(List.of(
                                                new Sequence(List.of(NON_TERMINALS.get(1))),
                                                new Sequence(List.of(NON_TERMINALS.get(2))),
                                                new Sequence(List.of(NON_TERMINALS.get(3)))))),
                                        NON_TERMINALS.get(4)))
                        )))));
        final BnfRuleGraph graph = new GraphBuilder(alternativesOneNodeEach).getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(4, graph.vertexSet().size());
        assertEquals(9, graph.edgeSet().size());

        final Scope startScope = graph.getStartScope();
        final Scope endScope = graph.getEndScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        final Scope secondScope = graph.getSuccessors(startScope).iterator().next();
        assertEquals(1, graph.inDegreeOf(secondScope));
        assertEquals(4, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(endScope).size());
        final Scope secondLastScope = graph.getPredecessors(endScope).iterator().next();
        assertEquals(4, graph.outDegreeOf(secondLastScope));
        assertEquals(7, graph.inDegreeOf(secondLastScope));

        final CycleDetector<Scope, ScopeEdge> cycleDetector = new CycleDetector<>(graph);
        assertTrue(cycleDetector.detectCycles());
        assertEquals(Set.of(secondLastScope), cycleDetector.findCycles());
        assertEquals(Set.of(endScope), graph.getSuccessors(secondLastScope));
        assertEquals(Set.of(endScope), graph.getOutGoingScopes(secondLastScope));
        assertEquals(Set.of(secondLastScope), graph.getSuccessors(secondScope));
    }

    @Test
    void graphBuilder_oneRule_loopInSequence() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Loop"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        NON_TERMINALS.get(0),
                                        new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(NON_TERMINALS.get(1)))))),
                                        NON_TERMINALS.get(2)
                                ))
                        )))));
        final BnfRuleGraph graph = new GraphBuilder(alternativesOneNodeEach).getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(4, graph.vertexSet().size());
        assertEquals(5, graph.edgeSet().size());

        assertEquals(1, graph.getSuccessors(graph.getStartScope()).size());
        final Scope secondScope = graph.getSuccessors(graph.getStartScope()).iterator().next();
        assertEquals(1, graph.inDegreeOf(secondScope));
        assertEquals(2, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(graph.getEndScope()).size());
        final Scope secondLastScope = graph.getPredecessors(graph.getEndScope()).iterator().next();
        assertEquals(3, graph.inDegreeOf(secondLastScope));
        assertEquals(2, graph.outDegreeOf(secondLastScope));

        final CycleDetector<Scope, ScopeEdge> cycleDetector = new CycleDetector<>(graph);
        assertTrue(cycleDetector.detectCycles());
        assertEquals(Set.of(secondLastScope), cycleDetector.findCycles());
    }

    @Test
    void graphBuilder_oneRule_nestedLoopInSequence() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Loop"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        NON_TERMINALS.get(0),
                                        new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(
                                                NON_TERMINALS.get(1),
                                                new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(NON_TERMINALS.get(2)))))),
                                                NON_TERMINALS.get(3)))))),
                                        NON_TERMINALS.get(4)))
                        )))));
        final BnfRuleGraph graph = new GraphBuilder(alternativesOneNodeEach).getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(6, graph.vertexSet().size());
        assertEquals(9, graph.edgeSet().size());

        final Scope startScope = graph.getStartScope();
        final Scope endScope = graph.getEndScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        final Scope secondScope = graph.getSuccessors(startScope).iterator().next();
        assertEquals(1, graph.inDegreeOf(secondScope));
        assertEquals(2, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(endScope).size());
        final Scope secondLastScope = graph.getPredecessors(endScope).iterator().next();
        assertEquals(2, graph.outDegreeOf(secondLastScope));
        assertEquals(2, graph.inDegreeOf(secondLastScope));

        assertEquals(1, graph.getSuccessors(secondScope).size());
        final Scope thirdScope = graph.getSuccessors(secondScope).iterator().next();
        assertEquals(2, graph.inDegreeOf(thirdScope));
        assertEquals(2, graph.outDegreeOf(thirdScope));
        final Scope thirdLastScope = graph.getPredecessors(secondLastScope).iterator().next();
        assertEquals(3, graph.inDegreeOf(thirdLastScope));
        assertEquals(2, graph.outDegreeOf(thirdLastScope));

        final CycleDetector<Scope, ScopeEdge> cycleDetector = new CycleDetector<>(graph);
        assertTrue(cycleDetector.detectCycles());
        assertEquals(Set.of(thirdLastScope, thirdScope, secondLastScope), cycleDetector.findCycles());
        assertEquals(Set.of(thirdScope, endScope), graph.getSuccessors(secondLastScope));
        assertEquals(Set.of(thirdScope, endScope), graph.getOutGoingScopes(secondLastScope));
        assertEquals(Set.of(secondLastScope), graph.getSuccessors(thirdLastScope));
        assertEquals(Set.of(secondLastScope), graph.getOutGoingScopes(thirdLastScope));
    }

    @Test
    void graphBuilder_oneRule_optionalInSequence() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Loop"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        NON_TERMINALS.get(0),
                                        new Optional(new Alternatives(List.of(
                                                new Sequence(List.of(NON_TERMINALS.get(1)))))),
                                        NON_TERMINALS.get(2)))
                        )))));
        final BnfRuleGraph graph = new GraphBuilder(alternativesOneNodeEach).getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(4, graph.vertexSet().size());
        assertEquals(4, graph.edgeSet().size());

        assertEquals(1, graph.getSuccessors(graph.getStartScope()).size());
        final Scope secondScope = graph.getSuccessors(graph.getStartScope()).iterator().next();
        assertEquals(1, graph.inDegreeOf(secondScope));
        assertEquals(2, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(graph.getEndScope()).size());
        final Scope secondLastScope = graph.getPredecessors(graph.getEndScope()).iterator().next();
        assertEquals(2, graph.inDegreeOf(secondLastScope));
        assertEquals(1, graph.outDegreeOf(secondLastScope));

        // Detect no cycles
        assertFalse(new CycleDetector<>(graph).detectCycles());
    }

    @Test
    void graphBuilder_oneRule_nestedOptionalInSequence() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Loop"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        NON_TERMINALS.get(0),
                                        new Optional(new Alternatives(List.of(new Sequence(List.of(
                                                NON_TERMINALS.get(1),
                                                new Optional(new Alternatives(List.of(new Sequence(List.of(NON_TERMINALS.get(2)))))),
                                                NON_TERMINALS.get(3)))))),
                                        NON_TERMINALS.get(4)))
                        )))));
        final BnfRuleGraph graph = new GraphBuilder(alternativesOneNodeEach).getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(6, graph.vertexSet().size());
        assertEquals(7, graph.edgeSet().size());

        final Scope startScope = graph.getStartScope();
        final Scope endScope = graph.getEndScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        final Scope secondScope = graph.getSuccessors(startScope).iterator().next();
        assertEquals(1, graph.inDegreeOf(secondScope));
        assertEquals(2, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(endScope).size());
        final Scope secondLastScope = graph.getPredecessors(endScope).iterator().next();
        assertEquals(1, graph.outDegreeOf(secondLastScope));
        assertEquals(2, graph.inDegreeOf(secondLastScope));

        assertFalse(new CycleDetector<>(graph).detectCycles());
    }

    @Test
    void graphBuilder_oneRule_alternativesInSequence() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Alternative"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        NON_TERMINALS.get(0),
                                        new Precedence(new Alternatives(List.of(
                                                new Sequence(List.of(NON_TERMINALS.get(1))),
                                                new Sequence(List.of(NON_TERMINALS.get(2)))))),
                                        NON_TERMINALS.get(3)))
                        )))));
        final GraphBuilder builder = new GraphBuilder(alternativesOneNodeEach);
        final BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(4, graph.edgeSet().size());
        assertEquals(4, graph.vertexSet().size());
        assertEquals(1, graph.getPredecessors(graph.getEndScope()).size());
        assertEquals(1, graph.getSuccessors(graph.getStartScope()).size());
    }

    @Test
    void graphBuilder_oneRule_alternativesEachOneNode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Alternative"),
                        new Alternatives(List.of(
                                new Sequence(List.of(NON_TERMINALS.get(0))),
                                new Sequence(List.of(NON_TERMINALS.get(1))),
                                new Sequence(List.of(NON_TERMINALS.get(2))),
                                new Sequence(List.of(NON_TERMINALS.get(3)))
                        )))));
        final GraphBuilder builder = new GraphBuilder(alternativesOneNodeEach);
        BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(2, graph.vertexSet().size());
        assertEquals(4, graph.edgeSet().size());

        final Scope startScope = graph.getStartScope();
        final Scope endScope = graph.getEndScope();
        assertEquals(4, graph.outDegreeOf(startScope));
        assertEquals(4, graph.inDegreeOf(endScope));

        assertEquals(endScope, graph.getSuccessors(startScope).iterator().next());
        assertEquals(startScope, graph.getPredecessors(endScope).iterator().next());
        assertEquals(Set.of(graph.getOutGoingNodes(startScope)), Set.of(graph.getInGoingNodes(endScope)));


        // Add new node to test addition
        Method processTextElement = GraphBuilder.class.getDeclaredMethod("processTextElement", TextElement.class);
        processTextElement.setAccessible(true);
        processTextElement.invoke(builder, new Keyword("New-Sequence"));
        graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.vertexSet().size());
        assertEquals(5, graph.edgeSet().size());

        assertEquals(4, graph.outDegreeOf(startScope));
        assertEquals(4, graph.inDegreeOf(endScope));

        assertEquals(endScope, graph.getSuccessors(startScope).iterator().next());
        assertEquals(startScope, graph.getPredecessors(endScope).iterator().next());
        assertEquals(graph.getOutGoingNodes(startScope), graph.getInGoingNodes(endScope));

        final Scope newEndScope = graph.getEndScope();
        assertEquals(startScope, graph.getStartScope());
        assertNotEquals(newEndScope, endScope);
        assertEquals(1, graph.inDegreeOf(newEndScope));
        assertEquals(1, graph.outDegreeOf(endScope));
        assertEquals(newEndScope, graph.getSuccessors(endScope).iterator().next());
        assertEquals(graph.getOutGoingNodes(endScope), graph.getInGoingNodes(newEndScope));
    }

    @Test
    void graphBuilder_oneRule_nestedAlternativesEachOneNode() {
        final Bnf nestedAlternativeWithOneNode = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Alternative"),
                        new Alternatives(List.of(
                                new Sequence(List.of(NON_TERMINALS.get(0))),
                                new Sequence(List.of(NON_TERMINALS.get(1))),
                                new Sequence(List.of(NON_TERMINALS.get(2))),
                                new Sequence(List.of(NON_TERMINALS.get(3))),
                                new Sequence(List.of(
                                        new Precedence(
                                                new Alternatives(List.of(
                                                        new Sequence(List.of(NON_TERMINALS.get(4))),
                                                        new Sequence(List.of(NON_TERMINALS.get(5))))))
                                )))))));
        final BnfRuleGraph graph = new GraphBuilder(nestedAlternativeWithOneNode).getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(6, graph.getOutGoingNodes(graph.getStartScope()).size());
        assertEquals(6, graph.getInGoingNodes(graph.getEndScope()).size());
        assertEquals(2, graph.vertexSet().size());
    }

    @Test
    void graphBuilder_oneRule_alternativeOfSequencesOfTwoNodes() {
        final Bnf alternativesOfSequenceOfTwo = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Alternative"),
                        new Alternatives(List.of(
                                new Sequence(List.of(NON_TERMINALS.get(0), NON_TERMINALS.get(1))),
                                new Sequence(List.of(NON_TERMINALS.get(2), NON_TERMINALS.get(3))),
                                new Sequence(List.of(NON_TERMINALS.get(4), NON_TERMINALS.get(5))),
                                new Sequence(List.of(NON_TERMINALS.get(6), NON_TERMINALS.get(7)))
                        )))));

        final GraphBuilder builder = new GraphBuilder(alternativesOfSequenceOfTwo);
        final BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(4, graph.getOutGoingNodes(graph.getStartScope()).size());
        assertEquals(4, graph.getInGoingNodes(graph.getEndScope()).size());
        assertEquals(8, graph.edgeSet().size());
        assertEquals(6, graph.vertexSet().size());
    }

    @Test
    void graphBuilder_oneRule_alternativesEachTwoNodes() {
        final Bnf nestedAlternativeEachTwoNodes = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Alternative"),
                        new Alternatives(List.of(
                                new Sequence(List.of(NON_TERMINALS.get(0))),
                                new Sequence(List.of(NON_TERMINALS.get(1))),
                                new Sequence(List.of(NON_TERMINALS.get(2))),
                                new Sequence(List.of(NON_TERMINALS.get(3))),
                                new Sequence(List.of(
                                        new Precedence(
                                                new Alternatives(List.of(
                                                        new Sequence(List.of(NON_TERMINALS.get(4), NON_TERMINALS.get(5))),
                                                        new Sequence(List.of(NON_TERMINALS.get(6), NON_TERMINALS.get(7)))
                                                ))))))))));
        final BnfRuleGraph graph = new GraphBuilder(nestedAlternativeEachTwoNodes).getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(8, graph.edgeSet().size());
        assertEquals(4, graph.vertexSet().size());
        assertEquals(6, graph.getInGoingNodes(graph.getEndScope()).size());
        assertEquals(6, graph.outgoingEdgesOf(graph.getStartScope()).size());
    }

    @Test
    void graphBuilder_oneRule_oneSequence() {
        final Bnf sequence = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Sequence"), new Alternatives(
                        List.of(new Sequence(List.of(NON_TERMINALS.get(0), NON_TERMINALS.get(1))))
                ))));
        final BnfRuleGraph graph = new GraphBuilder(sequence).getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.vertexSet().size());
        assertEquals(2, graph.edgeSet().size());

        final Scope startScope = graph.getStartScope();
        final Scope secondScope = graph.getSuccessors(startScope).iterator().next();
        final Scope endScope = graph.getSuccessors(secondScope).iterator().next();
        assertEquals(endScope, graph.getEndScope());

        assertEquals(NON_TERMINALS.get(0).getName(), graph.getOutGoingNodes(startScope).iterator().next().getName());
        assertEquals(NON_TERMINALS.get(1).getName(), graph.getOutGoingNodes(secondScope).iterator().next().getName());
    }

    @Test
    void replaceNonTerminals_noNonTerminals_doNothing() {
        final BnfRule sequence = new BnfRule(new NonTerminal("Sequence"),
                new Alternatives(List.of(new Sequence(
                        List.of(KEYWORDS.get(0), KEYWORDS.get(1), KEYWORDS.get(2), KEYWORDS.get(3))
                ))));
        final BnfRule idRule = new BnfRule(NON_TERMINALS.get(0),
                new Alternatives(List.of(new Sequence(List.of((KEYWORDS.get(3)))))));

        final GraphBuilder builder = new GraphBuilder(sequence);
        BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(4, graph.length());
        assertEquals(4, graph.edgeSet().size());
        assertEquals(5, graph.vertexSet().size());

        assertFalse(graph.containsNonTerminals());
        assertTrue(graph.getNonTerminalNodes().isEmpty());
        assertTrue(graph.getNonTerminalNodeEdges().isEmpty());

        builder.replaceNonTerminals(Set.of(new GraphBuilder(idRule).getGraph()));
        BnfRuleGraph graph1 = builder.getGraph();
        assertEquals(graph, graph1);
    }

    @Test
    void replaceNonTerminals_oneNonTerminals_replaceByTerminal() {
        final BnfRule sequence = new BnfRule(new NonTerminal("Sequence"),
                new Alternatives(List.of(new Sequence(
                        List.of(KEYWORDS.get(0), KEYWORDS.get(1), NON_TERMINALS.get(0), KEYWORDS.get(2))
                ))));
        final BnfRule idRule = new BnfRule(NON_TERMINALS.get(0),
                new Alternatives(List.of(new Sequence(List.of((KEYWORDS.get(3)))))));

        final GraphBuilder builder = new GraphBuilder(sequence);
        BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(4, graph.length());
        assertEquals(4, graph.edgeSet().size());
        assertEquals(5, graph.vertexSet().size());

        assertTrue(graph.containsNonTerminals());
        assertFalse(graph.getNonTerminalNodes().isEmpty());
        assertFalse(graph.getNonTerminalNodeEdges().isEmpty());

        builder.replaceNonTerminals(Set.of(new GraphBuilder(idRule).getGraph()));
        final BnfRuleGraph graph1 = builder.getGraph();
        assertNotEquals(graph, graph1);

        assertTrue(graph1.isConsistent());
        assertEquals(4, graph1.length());
        assertEquals(4, graph1.edgeSet().size());
        assertEquals(5, graph1.vertexSet().size());

        assertFalse(graph1.containsNonTerminals());
        assertTrue(graph1.getNonTerminalNodes().isEmpty());
        assertTrue(graph1.getNonTerminalNodeEdges().isEmpty());
    }

    @Test
    void replaceNonTerminalNodeEdge_replaceSingleEdgeWithAnother_graphIsStillSequence() {
        final BnfRule sequence = new BnfRule(new NonTerminal("Sequence"),
                        new Alternatives(List.of(new Sequence(
                                List.of(KEYWORDS.get(0), KEYWORDS.get(1), NON_TERMINALS.get(0), KEYWORDS.get(2))
                        ))));
        final BnfRule idRule = new BnfRule(NON_TERMINALS.get(0),
                new Alternatives(List.of(new Sequence(List.of((KEYWORDS.get(3)))))));

        final GraphBuilder builder = new GraphBuilder(sequence);
        BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(4, graph.length());
        assertEquals(4, graph.edgeSet().size());
        assertEquals(5, graph.vertexSet().size());

        assertTrue(graph.containsNonTerminals());
        assertFalse(graph.getNonTerminalNodes().isEmpty());
        assertFalse(graph.getNonTerminalNodeEdges().isEmpty());

        final Scope startScope = graph.getStartScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        final Scope secondScope = graph.getSuccessors(startScope).iterator().next();
        assertEquals(1, graph.getSuccessors(secondScope).size());
        final Scope thirdScope = graph.getSuccessors(secondScope).iterator().next();
        assertEquals(1, graph.getSuccessors(thirdScope).size());
        final Scope fourthScope = graph.getSuccessors(thirdScope).iterator().next();
        assertEquals(1, graph.getSuccessors(fourthScope).size());
        final Scope lastScope = graph.getSuccessors(fourthScope).iterator().next();
        assertEquals(graph.getEndScope(), lastScope);

        builder.replaceNonTerminals(Set.of(new GraphBuilder(idRule).getGraph()));
        graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(4, graph.length());
        assertEquals(4, graph.edgeSet().size());
        assertEquals(5, graph.vertexSet().size());

        assertFalse(graph.containsNonTerminals());
        assertTrue(graph.getNonTerminalNodes().isEmpty());
        assertTrue(graph.getNonTerminalNodeEdges().isEmpty());
    }

    @Test
    void replaceNonTerminalNodeEdge_replaceSingleEdgeWithInAlternatives_graphIsStillAlternative() {
        final BnfRule sequence = new BnfRule(new NonTerminal("Alternative"),
                new Alternatives(List.of(
                        new Sequence(List.of(KEYWORDS.get(0))),
                        new Sequence(List.of(NON_TERMINALS.get(0))),
                        new Sequence(List.of(NON_TERMINALS.get(1))),
                        new Sequence(List.of(KEYWORDS.get(1)))
                )));
        final BnfRule idRule0 = new BnfRule(NON_TERMINALS.get(0),
                new Alternatives(List.of(new Sequence(List.of((KEYWORDS.get(2)))))));
        final BnfRule idRule1 = new BnfRule(NON_TERMINALS.get(1),
                new Alternatives(List.of(new Sequence(List.of((KEYWORDS.get(3)))))));

        final GraphBuilder builder = new GraphBuilder(sequence);
        BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(1, graph.length());
        assertEquals(4, graph.edgeSet().size());
        assertEquals(2, graph.vertexSet().size());

        assertTrue(graph.containsNonTerminals());
        assertFalse(graph.getNonTerminalNodes().isEmpty());
        assertFalse(graph.getNonTerminalNodeEdges().isEmpty());

        final Scope startScope = graph.getStartScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        assertEquals(graph.getEndScope(), graph.getSuccessors(startScope).iterator().next());
        final Scope endScope = graph.getEndScope();
        assertEquals(1, graph.getPredecessors(endScope).size());
        assertEquals(graph.getStartScope(), graph.getPredecessors(endScope).iterator().next());

        builder.replaceNonTerminals(Set.of(new GraphBuilder(idRule0).getGraph(), new GraphBuilder(idRule1).getGraph()));
        graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(1, graph.length());
        assertEquals(4, graph.edgeSet().size());
        assertEquals(2, graph.vertexSet().size());

        assertFalse(graph.containsNonTerminals());
        assertTrue(graph.getNonTerminalNodes().isEmpty());
        assertTrue(graph.getNonTerminalNodeEdges().isEmpty());
    }

    @Test
    void replaceNonTerminalNodeEdge_replaceSingleEdgeAfterAlternatives_graphIsStillAlternatives() {
        final BnfRule sequence = new BnfRule(new NonTerminal("Alternative"),
                new Alternatives(List.of(
                        new Sequence(List.of(
                            new Precedence(new Alternatives(List.of(
                                new Sequence(List.of(KEYWORDS.get(0))),
                                new Sequence(List.of(KEYWORDS.get(1))),
                                new Sequence(List.of(KEYWORDS.get(2))),
                                new Sequence(List.of(KEYWORDS.get(3)))))),
                            NON_TERMINALS.get(0)
                )))));
        final BnfRule idRule0 = new BnfRule(NON_TERMINALS.get(0),
                new Alternatives(List.of(new Sequence(List.of((KEYWORDS.get(4)))))));

        final GraphBuilder builder = new GraphBuilder(sequence);
        BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(2, graph.length());
        assertEquals(5, graph.edgeSet().size());
        assertEquals(3, graph.vertexSet().size());

        assertTrue(graph.containsNonTerminals());
        assertFalse(graph.getNonTerminalNodes().isEmpty());
        assertFalse(graph.getNonTerminalNodeEdges().isEmpty());

        Scope startScope = graph.getStartScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        Scope secondScope = graph.getSuccessors(startScope).iterator().next();
        assertEquals(1, graph.getSuccessors(secondScope).size());
        assertEquals(graph.getEndScope(), graph.getSuccessors(secondScope).iterator().next());
        Scope endScope = graph.getEndScope();
        assertEquals(1, graph.getPredecessors(endScope).size());
        assertEquals(secondScope, graph.getPredecessors(endScope).iterator().next());

        builder.replaceNonTerminals(Set.of(new GraphBuilder(idRule0).getGraph()));
        graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(2, graph.length());
        assertEquals(5, graph.edgeSet().size());
        assertEquals(3, graph.vertexSet().size());

        assertFalse(graph.containsNonTerminals());
        assertTrue(graph.getNonTerminalNodes().isEmpty());
        assertTrue(graph.getNonTerminalNodeEdges().isEmpty());

        startScope = graph.getStartScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        secondScope = graph.getSuccessors(startScope).iterator().next();
        assertEquals(1, graph.getSuccessors(secondScope).size());
        assertEquals(graph.getEndScope(), graph.getSuccessors(secondScope).iterator().next());
        endScope = graph.getEndScope();
        assertEquals(1, graph.getPredecessors(endScope).size());
        assertEquals(secondScope, graph.getPredecessors(endScope).iterator().next());
    }
}
