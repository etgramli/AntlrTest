package de.etgramlich.antlr.util.graph.type;

import de.etgramlich.antlr.util.graph.type.node.Node;
import de.etgramlich.antlr.util.graph.type.node.SequenceNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphWrapperTest {
    private static final Scope FIRST_SCOPE = new Scope("Scope: First");
    private static final Scope SECOND_SCOPE = new Scope("Scope: Second");

    @Test
    void test_getEndScope_getStartScope_addOneNodeWithOneScope() {
        GraphWrapper wrapper = new GraphWrapper(FIRST_SCOPE.getName());

        wrapper.addSequence(SECOND_SCOPE, new SequenceNode("First SequenceNode"));

        assertTrue(wrapper.isGraphConsistent());
        assertEquals(SECOND_SCOPE, wrapper.getEndScope());
        assertEquals(FIRST_SCOPE, wrapper.getStartScope());
    }

    @Test
    void test_getSuccessors_twoNodes_returnsSecond() {
        GraphWrapper wrapper = new GraphWrapper(FIRST_SCOPE.getName());

        wrapper.addSequence(SECOND_SCOPE, new SequenceNode("First SequenceNode"));

        assertTrue(wrapper.isGraphConsistent());
        final Scope firstNode = wrapper.getStartScope();
        assertEquals(SECOND_SCOPE, wrapper.getSuccessors(firstNode).get(0));
    }

    @Test
    void test_getPredecessors_twoNodes_returnsSecond() {
        GraphWrapper wrapper = new GraphWrapper(FIRST_SCOPE.getName());

        wrapper.addSequence(SECOND_SCOPE, new SequenceNode("First SequenceNode"));

        assertTrue(wrapper.isGraphConsistent());
        final Scope endNode = wrapper.getEndScope();
        assertEquals(FIRST_SCOPE, wrapper.getPredecessors(endNode).get(0));
    }

    @Test
    void test_getEndScope_isGraphConsistent_emptyGraph_returnsTrue() {
        assertTrue(new GraphWrapper().isGraphConsistent());
    }
}