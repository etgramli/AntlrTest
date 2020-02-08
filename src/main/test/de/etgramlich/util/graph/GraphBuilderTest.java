package de.etgramlich.util.graph;

import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.Bnf;
import de.etgramlich.parser.type.BnfRule;
import de.etgramlich.parser.type.text.Keyword;
import de.etgramlich.parser.type.text.NonTerminal;
import de.etgramlich.parser.type.Sequence;
import de.etgramlich.util.graph.type.BnfRuleGraph;
import de.etgramlich.util.graph.type.Scope;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;

class GraphBuilderTest {
    private static final BnfRule START_RULE = new BnfRule(new NonTerminal("EntryPoint"),
            new Alternatives(List.of(new Sequence(List.of(new NonTerminal("FirstRule"))))));
    private static final Keyword TEXT_FIRST  = new Keyword("Text First");
    private static final Keyword TEXT_SECOND = new Keyword("Text Second");
    private static final NonTerminal ID_FIRST = new NonTerminal("ID First");
    private static final NonTerminal ID_SECOND = new NonTerminal("ID Second");


    private static final Bnf BNF_SEQUENCE;
    static {
        Sequence sequence = new Sequence(List.of(ID_FIRST, ID_SECOND));
        BNF_SEQUENCE = new Bnf(List.of(
                START_RULE,
                new BnfRule(new NonTerminal("Sequence"), new Alternatives(List.of(sequence)))
        ));
        // ToDo: build alternatives
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

        assertEquals(ID_FIRST.getName(), graph.getOutGoingNodes(startScope).get(0).getName());
        assertNull(graph.getOutGoingNodes(startScope).get(0).getSuccessor());
        assertEquals(ID_SECOND.getName(), graph.getOutGoingNodes(secondScope).get(0).getName());
        assertNull(graph.getOutGoingNodes(secondScope).get(0).getSuccessor());
    }
}
