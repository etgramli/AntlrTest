package de.etgramlich.dsl.graph.type;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BnfRuleGraphTest {

    private static final List<Scope> SCOPES;
    private static final int NUM_SCOPES = 6;

    private static final BnfRuleGraph SEQUENCE_GRAPH = new BnfRuleGraph(StringUtils.EMPTY);
    private static final BnfRuleGraph DIAMOND_GRAPH = new BnfRuleGraph(StringUtils.EMPTY);
    static {
        List<Scope> scopes = new ArrayList<>(NUM_SCOPES);
        for (int i = 0; i < NUM_SCOPES; ++i) {
            scopes.add(new Scope("S" + i));
        }
        SCOPES = Collections.unmodifiableList(scopes);

        SEQUENCE_GRAPH.addVertex(SCOPES.get(0));
        SEQUENCE_GRAPH.addVertex(SCOPES.get(1));
        SEQUENCE_GRAPH.addEdge(SCOPES.get(0), SCOPES.get(1), new NodeEdge(new Node("S0", NodeType.KEYWORD)));
        SEQUENCE_GRAPH.addVertex(SCOPES.get(2));
        SEQUENCE_GRAPH.addEdge(SCOPES.get(1), SCOPES.get(2), new NodeEdge(new Node("S1", NodeType.KEYWORD)));
        SEQUENCE_GRAPH.setStartScope(SCOPES.get(0));
        SEQUENCE_GRAPH.setEndScope(SCOPES.get(2));

        DIAMOND_GRAPH.addVertex(SCOPES.get(0));
        DIAMOND_GRAPH.addVertex(SCOPES.get(1));
        DIAMOND_GRAPH.addVertex(SCOPES.get(2));
        DIAMOND_GRAPH.addVertex(SCOPES.get(3));
        DIAMOND_GRAPH.addEdge(SCOPES.get(0), SCOPES.get(1), new NodeEdge(new Node("N0", NodeType.KEYWORD)));
        DIAMOND_GRAPH.addEdge(SCOPES.get(0), SCOPES.get(2), new NodeEdge(new Node("N1", NodeType.KEYWORD)));
        DIAMOND_GRAPH.addEdge(SCOPES.get(1), SCOPES.get(3), new NodeEdge(new Node("N2", NodeType.KEYWORD)));
        DIAMOND_GRAPH.addEdge(SCOPES.get(2), SCOPES.get(3), new NodeEdge(new Node("N3", NodeType.KEYWORD)));
        DIAMOND_GRAPH.setStartScope(SCOPES.get(0));
        DIAMOND_GRAPH.setEndScope(SCOPES.get(3));
    }

    @Test
    void isForwardEdge_edgeInAlternative_returnsTrue() {
        final Scope start = DIAMOND_GRAPH.getStartScope();
        final Scope second = DIAMOND_GRAPH.getSuccessors(start).iterator().next();

        assertTrue(DIAMOND_GRAPH.isForwardEdge(DIAMOND_GRAPH.getEdge(start, second)));
    }

    @Test
    void isBackwardEdge_edgeInAlternative_returnsFalse() {
        final Scope start = DIAMOND_GRAPH.getStartScope();
        final Scope second = DIAMOND_GRAPH.getSuccessors(start).iterator().next();

        assertFalse(DIAMOND_GRAPH.isBackwardEdge(DIAMOND_GRAPH.getEdge(start, second)));
    }

    @Test
    void length_emptyGraph_returnsZero() {
        assertEquals(0, new BnfRuleGraph(StringUtils.EMPTY).length());
    }

    @Test
    void length_sequenceGraph_returnsTwo() {
        assertEquals(2, SEQUENCE_GRAPH.length());
    }

    @Test
    void length_diamondGraph_returnsTwo() {
        assertEquals(2, DIAMOND_GRAPH.length());
    }

    @Test
    void isConsistent_emptyGraph_returnsTrue() {
        assertTrue(new BnfRuleGraph(StringUtils.EMPTY).isConsistent());
    }

    @Test
    void isConsistent_singleScope_returnsTrue() {
        final BnfRuleGraph graph = new BnfRuleGraph(StringUtils.EMPTY);
        final Scope scope = graph.addVertex();
        graph.setStartScope(scope);
        graph.setEndScope(scope);
        assertTrue(graph.isConsistent());
    }

    @Test
    void isConsistent_noStartScopeSet_returnsFalse() {
        final BnfRuleGraph graph = new BnfRuleGraph(StringUtils.EMPTY);
        final Scope scope = graph.addVertex();
        graph.setEndScope(scope);
        assertFalse(graph.isConsistent());
    }

    @Test
    void isConsistent_noEndScopeSet_returnsFalse() {
        final BnfRuleGraph graph = new BnfRuleGraph(StringUtils.EMPTY);
        final Scope scope = graph.addVertex();
        graph.setStartScope(scope);
        assertFalse(graph.isConsistent());
    }

    @Test
    void isConsistent_startScopeHasIngoingEdge_returnsFalse() {
        final BnfRuleGraph graph = new BnfRuleGraph(StringUtils.EMPTY);
        final Scope scope = graph.addVertex();
        final Scope second = graph.addVertex();
        graph.addEdge(scope, second, new OptionalEdge());
        graph.setStartScope(second);
        assertFalse(graph.isConsistent());
    }

    @Test
    void isConsistent_endScopeHasOutgoingEdge_returnsFalse() {
        final BnfRuleGraph graph = new BnfRuleGraph(StringUtils.EMPTY);
        final Scope scope = graph.addVertex();
        final Scope second = graph.addVertex();
        graph.addEdge(scope, second, new OptionalEdge());
        graph.setEndScope(scope);
        assertFalse(graph.isConsistent());
    }

    @Test
    void isConsistent_startAndEndScopeNotConnected_returnsFalse() {
        final BnfRuleGraph graph = new BnfRuleGraph(StringUtils.EMPTY);
        final Scope scope = graph.addVertex();
        final Scope second = graph.addVertex();
        graph.setStartScope(scope);
        graph.setEndScope(second);
        assertFalse(graph.isConsistent());
    }

    @Test
    void isConsistent_parallelOptionalEdges_returnsTrue() {
        final BnfRuleGraph graph = new BnfRuleGraph(StringUtils.EMPTY);
        final Scope scope = graph.addVertex();
        final Scope second = graph.addVertex();
        graph.addEdge(scope, second, new OptionalEdge());
        graph.addEdge(scope, second, new OptionalEdge());
        graph.setStartScope(scope);
        graph.setEndScope(second);
        assertTrue(graph.isConsistent());
    }

    @Test
    void getSuccessors_twoScopes_returnsOneSuccessor() {
        BnfRuleGraph graph = (BnfRuleGraph) SEQUENCE_GRAPH.clone();

        final Set<Scope> successors = graph.getSuccessors(SCOPES.get(0));
        assertTrue(graph.isConsistent());
        assertEquals(1, successors.size());
        assertEquals(SCOPES.get(1), successors.iterator().next());
    }

    @Test
    void getSuccessors_fourScopesAsDiamond_returnsTwoSuccessors() {
        BnfRuleGraph graph = (BnfRuleGraph) DIAMOND_GRAPH.clone();

        final Set<Scope> successors = graph.getSuccessors(SCOPES.get(0));
        assertTrue(graph.isConsistent());
        assertEquals(2, successors.size());
        assertTrue(successors.containsAll(Set.of(SCOPES.get(1), SCOPES.get(2))));
    }

    @Test
    void getPredecessors_diamondGraph_returnsTwoNodes() {
        BnfRuleGraph graph = (BnfRuleGraph) DIAMOND_GRAPH.clone();

        final Set<Scope> predecessors = graph.getPredecessors(SCOPES.get(3));
        assertTrue(graph.isConsistent());
        assertEquals(2, predecessors.size());
        assertTrue(predecessors.containsAll(Set.of(SCOPES.get(1), SCOPES.get(2))));
    }

    @Test
    void getOutGoingNodes_diamondGraph_twoOutgoingNodes() {
        BnfRuleGraph graph = (BnfRuleGraph) DIAMOND_GRAPH.clone();

        final Set<Node> successors = graph.getOutGoingNodes(SCOPES.get(0));
        assertTrue(graph.isConsistent());
        assertEquals(2, successors.size());
        assertTrue(successors.containsAll(Set.of(new Node("N0", NodeType.KEYWORD), new Node("N1", NodeType.KEYWORD))));
    }

    @Test
    void getStartScope() {
        assertEquals(SCOPES.get(0), SEQUENCE_GRAPH.getStartScope());
        assertEquals(SCOPES.get(0), DIAMOND_GRAPH.getStartScope());
    }

    @Test
    void getEndScope() {
        assertEquals(SCOPES.get(2), SEQUENCE_GRAPH.getEndScope());
        assertEquals(SCOPES.get(3), DIAMOND_GRAPH.getEndScope());
    }

    @Test
    void getDanglingNodeEdges() {
        BnfRuleGraph graph = new BnfRuleGraph(StringUtils.EMPTY);

        graph.addVertex(SCOPES.get(0));
        graph.addVertex(SCOPES.get(1));
        graph.addVertex(SCOPES.get(2));
        ScopeEdge edgeOne = new NodeEdge(new Node("N0", NodeType.KEYWORD));
        ScopeEdge edgeTwo = new NodeEdge(new Node("N1", NodeType.KEYWORD));
        graph.addEdge(SCOPES.get(0), SCOPES.get(1), edgeOne);
        graph.addEdge(SCOPES.get(0), SCOPES.get(2), edgeTwo);

        assertFalse(graph.isConsistent());
        Set<ScopeEdge> danglingNodes = graph.getDanglingScopeEdges();
        assertEquals(2, danglingNodes.size());
        assertTrue(danglingNodes.containsAll(Set.of(edgeOne, edgeTwo)));
    }

    @Test
    void getDanglingScopeEdges_sequenceUseStartNode_returnsEndScope() {
        final Scope start = SEQUENCE_GRAPH.getStartScope();
        assertEquals(SEQUENCE_GRAPH.incomingEdgesOf(SEQUENCE_GRAPH.getEndScope()),
                SEQUENCE_GRAPH.getDanglingScopeEdges(start));

        assertEquals(1, SEQUENCE_GRAPH.getSuccessors(start).size());
        final Scope second = SEQUENCE_GRAPH.getSuccessors(start).iterator().next();
        assertEquals(SEQUENCE_GRAPH.incomingEdgesOf(SEQUENCE_GRAPH.getEndScope()),
                SEQUENCE_GRAPH.getDanglingScopeEdges(second));
    }

    @Test
    void getDanglingScopeEdges_alternativesUseStartNode_returnsEndNode() {
        final Scope start = DIAMOND_GRAPH.getStartScope();
        assertEquals(DIAMOND_GRAPH.incomingEdgesOf(DIAMOND_GRAPH.getEndScope()),
                DIAMOND_GRAPH.getDanglingScopeEdges(start));

        assertEquals(2, DIAMOND_GRAPH.getSuccessors(start).size());
        final Scope second = DIAMOND_GRAPH.getSuccessors(start).iterator().next();
        assertEquals(DIAMOND_GRAPH.incomingEdgesOf(DIAMOND_GRAPH.getEndScope()),
                DIAMOND_GRAPH.getDanglingScopeEdges(second));
    }

    @Test
    void getDanglingScopeEdges_unfinishedAlternatives_returnsDanglingNodes() {
        final BnfRuleGraph unfinished = new BnfRuleGraph(StringUtils.EMPTY);
        unfinished.addVertex(SCOPES.get(0));
        unfinished.addVertex(SCOPES.get(1));
        unfinished.addVertex(SCOPES.get(2));
        unfinished.addVertex(SCOPES.get(3));
        final ScopeEdge edge0 = new NodeEdge(new Node("N0", NodeType.KEYWORD));
        final ScopeEdge edge1 = new NodeEdge(new Node("N1", NodeType.KEYWORD));
        final ScopeEdge edge2 = new NodeEdge(new Node("N2", NodeType.KEYWORD));
        unfinished.addEdge(SCOPES.get(0), SCOPES.get(1), edge0);
        unfinished.addEdge(SCOPES.get(0), SCOPES.get(2), edge1);
        unfinished.addEdge(SCOPES.get(0), SCOPES.get(3), edge2);

        assertEquals(Set.of(edge0, edge1, edge2), unfinished.getDanglingScopeEdges(SCOPES.get(0)));
    }

    @Test
    void getDanglingScopeEdges_unfinishedNestedAlternatives_returnsDanglingNodesOfAllOrInnerAlternatives() {
        final BnfRuleGraph unfinished = new BnfRuleGraph(StringUtils.EMPTY);
        unfinished.addVertex(SCOPES.get(0));
        unfinished.setStartScope(SCOPES.get(0));
        unfinished.addVertex(SCOPES.get(1));
        unfinished.addVertex(SCOPES.get(2));
        unfinished.addVertex(SCOPES.get(3));
        final ScopeEdge edge0 = new NodeEdge(new Node("N0", NodeType.KEYWORD));
        final ScopeEdge edge1 = new NodeEdge(new Node("N1", NodeType.KEYWORD));
        final ScopeEdge edge2 = new NodeEdge(new Node("N2", NodeType.KEYWORD));
        unfinished.addEdge(SCOPES.get(0), SCOPES.get(1), edge0);
        unfinished.addEdge(SCOPES.get(0), SCOPES.get(2), edge1);
        unfinished.addEdge(SCOPES.get(0), SCOPES.get(3), edge2);

        unfinished.addVertex(SCOPES.get(4));
        unfinished.addVertex(SCOPES.get(5));
        final ScopeEdge edge3 = new NodeEdge(new Node("N3", NodeType.KEYWORD));
        final ScopeEdge edge4 = new NodeEdge(new Node("N4", NodeType.KEYWORD));
        unfinished.addEdge(SCOPES.get(3), SCOPES.get(4), edge3);
        unfinished.addEdge(SCOPES.get(3), SCOPES.get(5), edge4);

        assertFalse(unfinished.isConsistent());
        assertEquals(Set.of(edge3, edge4), unfinished.getDanglingScopeEdges(SCOPES.get(3)));
        assertEquals(Set.of(edge0, edge1, edge3, edge4),
                     unfinished.getDanglingScopeEdges(unfinished.getStartScope()));
    }

    @Test
    void getReadableString_startScope_returnsBeginScope() {
        assertEquals("BeginScope", SEQUENCE_GRAPH.getReadableString(SEQUENCE_GRAPH.getStartScope()));
        assertEquals("BeginScope", SEQUENCE_GRAPH.getReadableString(SCOPES.get(0)));
    }

    @Test
    void getReadableString_endScope_returnsEndScope() {
        assertEquals("EndScope", SEQUENCE_GRAPH.getReadableString(SEQUENCE_GRAPH.getEndScope()));
        assertEquals("EndScope", SEQUENCE_GRAPH.getReadableString(SCOPES.get(2)));
    }

    @Test
    void getReadableString_sequence() {
        final BnfRuleGraph graph = new BnfRuleGraph("sequence");
        final Scope firstScope = new Scope("FirstScope");
        final Scope secondScope = new Scope("SecondScope");
        final Scope thirdScope = new Scope("ThirdScope");
        final NodeEdge firstEdge = new NodeEdge(new Node("firstNodeEdge", NodeType.KEYWORD));
        final NodeEdge secondEdge = new NodeEdge(new Node("secondNodeEdge", NodeType.KEYWORD));
        graph.addVertex(firstScope);
        graph.addVertex(secondScope);
        graph.addVertex(thirdScope);
        graph.addEdge(firstScope, secondScope, firstEdge);
        graph.addEdge(secondScope, thirdScope, secondEdge);

        assertEquals("FirstNodeEdgeScope", graph.getReadableString(firstScope));
        assertEquals(thirdScope.getName(), graph.getReadableString(thirdScope));

        graph.setStartScope(firstScope);
        graph.setEndScope(thirdScope);
        assertEquals("BeginScope", graph.getReadableString(firstScope));
        assertEquals("SecondNodeEdgeScope", graph.getReadableString(secondScope));
        assertEquals("EndScope", graph.getReadableString(thirdScope));
    }

    @Test
    void getReadableString_alternative() {
        final BnfRuleGraph graph = new BnfRuleGraph("alternative");
        final Scope firstScope = new Scope("FirstScope");
        final Scope secondScope = new Scope("SecondScope");
        final Scope thirdScope = new Scope("ThirdScope");
        final NodeEdge firstEdge = new NodeEdge(new Node("firstNodeEdge", NodeType.KEYWORD));
        final NodeEdge firstParallelEdge = new NodeEdge(new Node("firstParallelNodeEdge", NodeType.KEYWORD));
        final NodeEdge secondParallelEdge = new NodeEdge(new Node("secondParallelNodeEdge", NodeType.KEYWORD));
        final NodeEdge thirdParallelEdge = new NodeEdge(new Node("thirdParallelNodeEdge", NodeType.KEYWORD));
        graph.addVertex(firstScope);
        graph.addVertex(secondScope);
        graph.addVertex(thirdScope);
        graph.addEdge(firstScope, secondScope, firstEdge);
        graph.addEdge(secondScope, thirdScope, firstParallelEdge);
        graph.addEdge(secondScope, thirdScope, secondParallelEdge);
        graph.addEdge(secondScope, thirdScope, thirdParallelEdge);
        graph.setStartScope(firstScope);
        graph.setEndScope(thirdScope);

        assertEquals("BeginScope", graph.getReadableString(firstScope));
        assertEquals("EndScope", graph.getReadableString(thirdScope));
        final Set<String> expectedStrings = Set.of(
                "FirstParallelNodeEdgeSecondParallelNodeEdgeThirdParallelNodeEdgeScope",
                "FirstParallelNodeEdgeThirdParallelNodeEdgeSecondParallelNodeEdgeScope",
                "SecondParallelNodeEdgeFirstParallelNodeEdgeThirdParallelNodeEdgeScope",
                "SecondParallelNodeEdgeThirdParallelNodeEdgeFirstParallelNodeEdgeScope",
                "ThirdParallelNodeEdgeFirstParallelNodeEdgeSecondParallelNodeEdgeScope",
                "ThirdParallelNodeEdgeSecondParallelNodeEdgeFirstParallelNodeEdgeScope");
        assertTrue(expectedStrings.contains(graph.getReadableString(secondScope)));
    }

    @Test
    void getReadableString_loop() {
        final BnfRuleGraph graph = new BnfRuleGraph("sequence");
        final Scope firstScope = new Scope("FirstScope");
        final Scope secondScope = new Scope("SecondScope");
        final Scope thirdScope = new Scope("ThirdScope");
        final NodeEdge loopEdge = new NodeEdge(new Node("loopNodeEdge", NodeType.KEYWORD));
        graph.addVertex(firstScope);
        graph.addVertex(secondScope);
        graph.addVertex(thirdScope);
        graph.addEdge(firstScope, secondScope, new OptionalEdge());
        graph.addEdge(secondScope, thirdScope, new OptionalEdge());
        graph.addEdge(secondScope, secondScope, loopEdge);
        graph.setStartScope(firstScope);
        graph.setEndScope(thirdScope);

        assertEquals("BeginScope", graph.getReadableString(firstScope));
        assertEquals("EndScope", graph.getReadableString(thirdScope));
        assertEquals("LoopNodeEdgeScope", graph.getReadableString(secondScope));
    }
}
