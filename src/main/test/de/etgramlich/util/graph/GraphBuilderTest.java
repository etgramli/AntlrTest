package de.etgramlich.util.graph;

import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.Bnf;
import de.etgramlich.parser.type.BnfRule;
import de.etgramlich.parser.type.repetition.Optional;
import de.etgramlich.parser.type.repetition.Precedence;
import de.etgramlich.parser.type.repetition.ZeroOrMore;
import de.etgramlich.parser.type.text.Keyword;
import de.etgramlich.parser.type.text.NonTerminal;
import de.etgramlich.parser.type.Sequence;
import de.etgramlich.util.graph.type.BnfRuleGraph;
import de.etgramlich.util.graph.type.Scope;
import de.etgramlich.util.graph.type.ScopeEdge;
import de.etgramlich.util.graph.type.node.SequenceNode;
import org.jgrapht.alg.cycle.CycleDetector;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

class GraphBuilderTest {
    private static final BnfRule START_RULE = new BnfRule(
            new NonTerminal("EntryPoint"),
            new Alternatives(List.of(new Sequence(List.of(new NonTerminal("FirstRule"))))));
    private static final NonTerminal ID_0 = new NonTerminal("ID_0");
    private static final NonTerminal ID_1 = new NonTerminal("ID_1");
    private static final NonTerminal ID_2 = new NonTerminal("ID_2");
    private static final NonTerminal ID_3 = new NonTerminal("ID_3");
    private static final NonTerminal ID_4 = new NonTerminal("ID_4");
    private static final NonTerminal ID_5 = new NonTerminal("ID_5");
    private static final NonTerminal ID_6 = new NonTerminal("ID_6");
    private static final NonTerminal ID_7 = new NonTerminal("ID_7");

    @Test
    void graphBuilder_oneRule_joi() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("component"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        new Precedence(new Alternatives(List.of(
                                                new Sequence(List.of(new Keyword("component"))),
                                                new Sequence(List.of(new Keyword("singleton")))))),
                                        new NonTerminal("componentName"),
                                        new NonTerminal("componentInterface"),
                                        new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(new NonTerminal("componentInterface")))))),
                                        new NonTerminal("componentMethod"),
                                        new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(new NonTerminal("componentMethod")))))),
                                        new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(new NonTerminal("componentField"))))))
                                ))
                        )))));
        final GraphBuilder builder = new GraphBuilder(alternativesOneNodeEach);
        final BnfRuleGraph graph = builder.getGraph();

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
                                        ID_0,
                                        new Precedence(new Alternatives(List.of(
                                                new Sequence(List.of(ID_1)),
                                                new Sequence(List.of(new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(ID_2))))))),
                                                new Sequence(List.of(ID_3))))),
                                        ID_4))
                        )))));
        final GraphBuilder builder = new GraphBuilder(alternativesOneNodeEach);
        final BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(4, graph.vertexSet().size());
        assertEquals(7, graph.edgeSet().size());

        final Scope startScope = graph.getStartScope();
        final Scope endScope = graph.getEndScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        final Scope secondScope = graph.getSuccessors(startScope).get(0);
        assertEquals(2, graph.inDegreeOf(secondScope));
        assertEquals(4, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(endScope).size());
        final Scope secondLastScope = graph.getPredecessors(endScope).get(0);
        assertEquals(2, graph.outDegreeOf(secondLastScope));
        assertEquals(4, graph.inDegreeOf(secondLastScope));

        final CycleDetector<Scope, ScopeEdge> cycleDetector = new CycleDetector<>(graph);
        assertTrue(cycleDetector.detectCycles());
        assertEquals(Set.of(secondScope, secondLastScope), cycleDetector.findCycles());
        assertEquals(Set.of(secondScope, endScope), Set.copyOf(graph.getSuccessors(secondLastScope)));
        assertEquals(Set.of(secondLastScope), Set.copyOf(graph.getSuccessors(secondScope)));
    }

    @Test
    void graphBuilder_oneRule_alternativeInLoop() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Loop"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        ID_0,
                                        new ZeroOrMore(new Alternatives(List.of(
                                                new Sequence(List.of(ID_1)),
                                                new Sequence(List.of(ID_2)),
                                                new Sequence(List.of(ID_3))))),
                                        ID_4))
                        )))));
        final GraphBuilder builder = new GraphBuilder(alternativesOneNodeEach);
        final BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(4, graph.vertexSet().size());
        assertEquals(7, graph.edgeSet().size());

        final Scope startScope = graph.getStartScope();
        final Scope endScope = graph.getEndScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        final Scope secondScope = graph.getSuccessors(startScope).get(0);
        assertEquals(2, graph.inDegreeOf(secondScope));
        assertEquals(4, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(endScope).size());
        final Scope secondLastScope = graph.getPredecessors(endScope).get(0);
        assertEquals(2, graph.outDegreeOf(secondLastScope));
        assertEquals(4, graph.inDegreeOf(secondLastScope));

        final CycleDetector<Scope, ScopeEdge> cycleDetector = new CycleDetector<>(graph);
        assertTrue(cycleDetector.detectCycles());
        assertEquals(Set.of(secondScope, secondLastScope), cycleDetector.findCycles());
        assertEquals(Set.of(secondScope, endScope), Set.copyOf(graph.getSuccessors(secondLastScope)));
        assertEquals(Set.of(secondLastScope), Set.copyOf(graph.getSuccessors(secondScope)));
    }

    @Test
    void graphBuilder_oneRule_loopInSequence() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Loop"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        ID_0,
                                        new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(ID_1))))),
                                        ID_2
                                ))
                        )))));
        final GraphBuilder builder = new GraphBuilder(alternativesOneNodeEach);
        final BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(4, graph.vertexSet().size());
        assertEquals(5, graph.edgeSet().size());

        assertEquals(1, graph.getSuccessors(graph.getStartScope()).size());
        final Scope secondScope = graph.getSuccessors(graph.getStartScope()).get(0);
        assertEquals(2, graph.inDegreeOf(secondScope));
        assertEquals(2, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(graph.getEndScope()).size());
        final Scope secondLastScope = graph.getPredecessors(graph.getEndScope()).get(0);
        assertEquals(2, graph.inDegreeOf(secondLastScope));
        assertEquals(2, graph.outDegreeOf(secondLastScope));

        final CycleDetector<Scope, ScopeEdge> cycleDetector = new CycleDetector<>(graph);
        assertTrue(cycleDetector.detectCycles());
        assertEquals(Set.of(secondScope, secondLastScope), cycleDetector.findCycles());
    }

    @Test
    void graphBuilder_oneRule_nestedLoopInSequence() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Loop"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        ID_0,
                                        new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(
                                                ID_1,
                                                new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(ID_2))))),
                                                ID_3))))),
                                        ID_4))
                        )))));
        final GraphBuilder builder = new GraphBuilder(alternativesOneNodeEach);
        final BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(6, graph.vertexSet().size());
        assertEquals(9, graph.edgeSet().size());

        final Scope startScope = graph.getStartScope();
        final Scope endScope = graph.getEndScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        final Scope secondScope = graph.getSuccessors(startScope).get(0);
        assertEquals(2, graph.inDegreeOf(secondScope));
        assertEquals(2, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(endScope).size());
        final Scope secondLastScope = graph.getPredecessors(endScope).get(0);
        assertEquals(2, graph.outDegreeOf(secondLastScope));
        assertEquals(2, graph.inDegreeOf(secondLastScope));

        assertEquals(2, graph.getSuccessors(secondScope).size());
        final Scope thirdScope = graph.getSuccessors(secondScope).get(0);
        assertEquals(1, graph.getPredecessors(secondLastScope).size());
        final Scope thirdLastScope = graph.getPredecessors(secondLastScope).get(0);

        final CycleDetector<Scope, ScopeEdge> cycleDetector = new CycleDetector<>(graph);
        assertTrue(cycleDetector.detectCycles());
        assertEquals(Set.of(thirdLastScope, thirdScope, secondScope, secondLastScope), cycleDetector.findCycles());
        assertEquals(Set.of(secondScope, endScope), Set.copyOf(graph.getSuccessors(secondLastScope)));
        assertEquals(Set.of(thirdScope, secondLastScope), Set.copyOf(graph.getSuccessors(thirdLastScope)));
    }

    @Test
    void graphBuilder_oneRule_optionalInSequence() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Loop"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        ID_0,
                                        new Optional(new Alternatives(List.of(
                                                new Sequence(List.of(ID_1))))),
                                        ID_2))
                        )))));
        final GraphBuilder builder = new GraphBuilder(alternativesOneNodeEach);
        final BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(4, graph.vertexSet().size());
        assertEquals(4, graph.edgeSet().size());

        assertEquals(1, graph.getSuccessors(graph.getStartScope()).size());
        final Scope secondScope = graph.getSuccessors(graph.getStartScope()).get(0);
        assertEquals(1, graph.inDegreeOf(secondScope));
        assertEquals(2, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(graph.getEndScope()).size());
        final Scope secondLastScope = graph.getPredecessors(graph.getEndScope()).get(0);
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
                                        ID_0,
                                        new Optional(new Alternatives(List.of(new Sequence(List.of(
                                                ID_1,
                                                new Optional(new Alternatives(List.of(new Sequence(List.of(ID_2))))),
                                                ID_3))))),
                                        ID_4))
                        )))));
        final GraphBuilder builder = new GraphBuilder(alternativesOneNodeEach);
        final BnfRuleGraph graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.length());
        assertEquals(6, graph.vertexSet().size());
        assertEquals(7, graph.edgeSet().size());

        final Scope startScope = graph.getStartScope();
        final Scope endScope = graph.getEndScope();
        assertEquals(1, graph.getSuccessors(startScope).size());
        final Scope secondScope = graph.getSuccessors(startScope).get(0);
        assertEquals(1, graph.inDegreeOf(secondScope));
        assertEquals(2, graph.outDegreeOf(secondScope));

        assertEquals(1, graph.getPredecessors(endScope).size());
        final Scope secondLastScope = graph.getPredecessors(endScope).get(0);
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
                                        ID_0,
                                        new Precedence(new Alternatives(List.of(
                                                new Sequence(List.of(ID_1)),
                                                new Sequence(List.of(ID_2))))),
                                        ID_3))
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
    void graphBuilder_oneRule_alternativesEachOneNode() {
        final Bnf alternativesOneNodeEach = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Alternative"),
                        new Alternatives(List.of(
                                new Sequence(List.of(ID_0)),
                                new Sequence(List.of(ID_1)),
                                new Sequence(List.of(ID_2)),
                                new Sequence(List.of(ID_3))
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

        assertEquals(endScope, graph.getSuccessors(startScope).get(0));
        assertEquals(startScope, graph.getPredecessors(endScope).get(0));
        assertEquals(graph.getOutGoingNodes(startScope), graph.getInGoingNodes(endScope));


        // Add new node to test addition
        builder.addNodeInSequence(new SequenceNode("New-Sequence"));
        graph = builder.getGraph();

        assertTrue(graph.isConsistent());
        assertEquals(3, graph.vertexSet().size());
        assertEquals(5, graph.edgeSet().size());

        assertEquals(4, graph.outDegreeOf(startScope));
        assertEquals(4, graph.inDegreeOf(endScope));

        assertEquals(endScope, graph.getSuccessors(startScope).get(0));
        assertEquals(startScope, graph.getPredecessors(endScope).get(0));
        assertEquals(graph.getOutGoingNodes(startScope), graph.getInGoingNodes(endScope));

        final Scope newEndScope = graph.getEndScope();
        assertEquals(startScope, graph.getStartScope());
        assertNotEquals(newEndScope, endScope);
        assertEquals(1, graph.inDegreeOf(newEndScope));
        assertEquals(1, graph.outDegreeOf(endScope));
        assertEquals(newEndScope, graph.getSuccessors(endScope).get(0));
        assertEquals(graph.getOutGoingNodes(endScope), graph.getInGoingNodes(newEndScope));
    }

    @Test
    void graphBuilder_oneRule_nestedAlternativesEachOneNode() {
        final Bnf nestedAlternativeWithOneNode = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Alternative"),
                        new Alternatives(List.of(
                                new Sequence(List.of(ID_0)),
                                new Sequence(List.of(ID_1)),
                                new Sequence(List.of(ID_2)),
                                new Sequence(List.of(ID_3)),
                                new Sequence(List.of(
                                        new Precedence(
                                                new Alternatives(List.of(
                                                        new Sequence(List.of(ID_4)),
                                                        new Sequence(List.of(ID_5)))))
                                )))))));

        final GraphBuilder graphBuilder = new GraphBuilder(nestedAlternativeWithOneNode);
        final BnfRuleGraph graph = graphBuilder.getGraph();

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
                                new Sequence(List.of(ID_0, ID_1)),
                                new Sequence(List.of(ID_2, ID_3)),
                                new Sequence(List.of(ID_4, ID_5)),
                                new Sequence(List.of(ID_6, ID_7))
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
                                new Sequence(List.of(ID_0)),
                                new Sequence(List.of(ID_1)),
                                new Sequence(List.of(ID_2)),
                                new Sequence(List.of(ID_3)),
                                new Sequence(List.of(
                                        new Precedence(
                                                new Alternatives(List.of(
                                                        new Sequence(List.of(ID_4, ID_5)),
                                                        new Sequence(List.of(ID_6, ID_7))
                                                ))))))))));
        final GraphBuilder builder = new GraphBuilder(nestedAlternativeEachTwoNodes);
        final BnfRuleGraph graph = builder.getGraph();

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
                        List.of(new Sequence(List.of(ID_0, ID_1)))
                ))));

        final GraphBuilder builder = new GraphBuilder(sequence);
        final BnfRuleGraph graph = builder.getGraph();

        // Graph consistency test
        assertTrue(graph.isConsistent());
        assertEquals(3, graph.vertexSet().size());
        assertEquals(2, graph.edgeSet().size());

        // Test of correct objects arrangement
        final Scope startScope = graph.getStartScope();
        final Scope secondScope = graph.getSuccessors(startScope).get(0);
        final Scope endScope = graph.getSuccessors(secondScope).get(0);
        assertEquals(endScope, graph.getEndScope());

        assertEquals(ID_0.getName(), graph.getOutGoingNodes(startScope).get(0).getName());
        assertEquals(ID_1.getName(), graph.getOutGoingNodes(secondScope).get(0).getName());
    }
}
