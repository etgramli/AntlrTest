package de.etgramlich.util.graph.type;

import de.etgramlich.util.graph.type.node.Node;
import de.etgramlich.util.graph.type.node.SequenceNode;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BnfRuleGraphTest {

    private static final List<Scope> SCOPES =
            List.of(new Scope("S0"), new Scope("S1"), new Scope("S2"), new Scope("S3"));

    private static final BnfRuleGraph SEQUENCE_GRAPH = new BnfRuleGraph();
    private static final BnfRuleGraph DIAMOND_GRAPH = new BnfRuleGraph();
    static {
        SEQUENCE_GRAPH.addVertex(SCOPES.get(0));
        SEQUENCE_GRAPH.addVertex(SCOPES.get(1));
        SEQUENCE_GRAPH.addEdge(SCOPES.get(0), SCOPES.get(1), new ScopeEdge(SCOPES.get(0), SCOPES.get(1), new SequenceNode("S0")));

        DIAMOND_GRAPH.addVertex(SCOPES.get(0));
        DIAMOND_GRAPH.addVertex(SCOPES.get(1));
        DIAMOND_GRAPH.addVertex(SCOPES.get(2));
        DIAMOND_GRAPH.addVertex(SCOPES.get(3));
        DIAMOND_GRAPH.addEdge(SCOPES.get(0), SCOPES.get(1), new ScopeEdge(SCOPES.get(0), SCOPES.get(1), new SequenceNode("N0")));
        DIAMOND_GRAPH.addEdge(SCOPES.get(0), SCOPES.get(2), new ScopeEdge(SCOPES.get(0), SCOPES.get(2), new SequenceNode("N1")));
        DIAMOND_GRAPH.addEdge(SCOPES.get(1), SCOPES.get(3), new ScopeEdge(SCOPES.get(1), SCOPES.get(3), new SequenceNode("N2")));
        DIAMOND_GRAPH.addEdge(SCOPES.get(2), SCOPES.get(3), new ScopeEdge(SCOPES.get(2), SCOPES.get(3), new SequenceNode("N3")));
    }

    @Test
    void isConsistent_emptyGraph_returnsTrue() {
        BnfRuleGraph graph = new BnfRuleGraph();

        assertTrue(graph.isConsistent());
    }

    @Test
    void getSuccessors_twoScopes_returnsOneSuccessor() {
        BnfRuleGraph graph = (BnfRuleGraph) SEQUENCE_GRAPH.clone();

        final List<Scope> successors = graph.getSuccessors(SCOPES.get(0));
        assertTrue(graph.isConsistent());
        assertEquals(1, successors.size());
        assertEquals(SCOPES.get(1), successors.get(0));
    }

    @Test
    void getSuccessors_fourScopesAsDiamond_returnsTwoSuccessors() {
        BnfRuleGraph graph = (BnfRuleGraph) DIAMOND_GRAPH.clone();

        final List<Scope> successors = graph.getSuccessors(SCOPES.get(0));
        assertTrue(graph.isConsistent());
        assertEquals(2, successors.size());
        assertTrue(successors.containsAll(Set.of(SCOPES.get(1), SCOPES.get(2))));
    }

    @Test
    void getPredecessors_diamondGraph_returnsTwoNodes() {
        BnfRuleGraph graph = (BnfRuleGraph) DIAMOND_GRAPH.clone();

        final List<Scope> predecessors = graph.getPredecessors(SCOPES.get(3));
        assertTrue(graph.isConsistent());
        assertEquals(2, predecessors.size());
        assertTrue(predecessors.containsAll(Set.of(SCOPES.get(1), SCOPES.get(2))));
    }

    @Test
    void getOutGoingNodes_diamondGraph_twoOutgoingNodes() {
        BnfRuleGraph graph = (BnfRuleGraph) DIAMOND_GRAPH.clone();

        final List<Node> successors = graph.getOutGoingNodes(SCOPES.get(0));
        assertTrue(graph.isConsistent());
        assertEquals(2, successors.size());
        assertTrue(successors.containsAll(Set.of(new SequenceNode("N0"), new SequenceNode("N1"))));
    }

    @Test
    void getStartScope() {
        assertEquals(SCOPES.get(0), SEQUENCE_GRAPH.getStartScope());
        assertEquals(SCOPES.get(0), DIAMOND_GRAPH.getStartScope());
    }

    @Test
    void getEndScope() {
        assertEquals(SCOPES.get(1), SEQUENCE_GRAPH.getEndScope());
        assertEquals(SCOPES.get(3), DIAMOND_GRAPH.getEndScope());
    }

    @Test
    void getDanglingNodeEdges() {
        BnfRuleGraph graph = new BnfRuleGraph();

        graph.addVertex(SCOPES.get(0));
        graph.addVertex(SCOPES.get(1));
        graph.addVertex(SCOPES.get(2));
        ScopeEdge edgeOne = new ScopeEdge(SCOPES.get(0), SCOPES.get(1), new SequenceNode("N0"));
        ScopeEdge edgeTwo = new ScopeEdge(SCOPES.get(0), SCOPES.get(2), new SequenceNode("N1"));
        graph.addEdge(SCOPES.get(0), SCOPES.get(1), edgeOne);
        graph.addEdge(SCOPES.get(0), SCOPES.get(2), edgeTwo);

        assertFalse(graph.isConsistent());
        Set<ScopeEdge> danglingNodes = graph.getDanglingNodeEdges();
        assertEquals(2, danglingNodes.size());
        assertTrue(danglingNodes.containsAll(Set.of(edgeOne, edgeTwo)));
    }
}
