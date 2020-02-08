package de.etgramlich.util.graph;

import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.Bnf;
import de.etgramlich.parser.type.BnfRule;
import de.etgramlich.parser.type.text.Keyword;
import de.etgramlich.parser.type.text.NonTerminal;
import de.etgramlich.parser.type.Sequence;
import de.etgramlich.util.graph.type.BnfRuleGraph;
import de.etgramlich.util.graph.type.Scope;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;

class GraphBuilderTest {
    private static final BnfRule START_RULE = new BnfRule(new NonTerminal("EntryPoint"),
            new Alternatives(List.of(new Sequence(List.of(new NonTerminal("FirstRule"))))));
    private static final Keyword TEXT_FIRST  = new Keyword("Text First");
    private static final Keyword TEXT_SECOND = new Keyword("Text Second");
    private static final NonTerminal ID_0 = new NonTerminal("ID_0");
    private static final NonTerminal ID_1 = new NonTerminal("ID_1");
    private static final NonTerminal ID_2 = new NonTerminal("ID_2");
    private static final NonTerminal ID_3 = new NonTerminal("ID_3");

    private static final Bnf BNF_SEQUENCE = new Bnf(List.of(
            START_RULE,
            new BnfRule(new NonTerminal("Sequence"), new Alternatives(
                    List.of(new Sequence(List.of(ID_0, ID_1)))))
    ));
    private static final Bnf BNF_ALTERNATIVES_ONE_NODE = new Bnf(List.of(
            START_RULE,
            new BnfRule(new NonTerminal("Alternative"),
                    new Alternatives(List.of(
                            new Sequence(List.of(ID_0)),
                            new Sequence(List.of(ID_1)),
                            new Sequence(List.of(ID_2)),
                            new Sequence(List.of(ID_3))
    )))));

    // ToDo: build nested alternatives
    // ToDo: loop

    @Test
    void graphBuilder_oneRule_alternativesEachOneNode() {
        final GraphBuilder builder = new GraphBuilder(BNF_ALTERNATIVES_ONE_NODE);
        final BnfRuleGraph graph = builder.getGraph();

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
    }

    @Test
    @Disabled
    void graphBuilder_oneRule_nestedAlternativesEachOneNode() {
        // ToDo
    }

    @Test
    @Disabled
    void graphBuilder_oneRule_alternativesEachTwoNodes() {
        // ToDo
    }

    @Test
    void graphBuilder_oneRule_oneSequence() {
        final GraphBuilder builder = new GraphBuilder(BNF_SEQUENCE);
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
        assertNull(graph.getOutGoingNodes(startScope).get(0).getSuccessor());
        assertEquals(ID_1.getName(), graph.getOutGoingNodes(secondScope).get(0).getName());
        assertNull(graph.getOutGoingNodes(secondScope).get(0).getSuccessor());
    }
}
