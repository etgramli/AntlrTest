package de.etgramlich.util.graph.type;

import de.etgramlich.util.graph.type.node.SequenceNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphWrapperTest {
    private static final String START_SCOPE_NAME = "Scope_Start";
    private static final SequenceNode FIRST_SEQUENCE_NODE = new SequenceNode("First SequenceNode");

    @Test
    void test_getEndScope_getStartScope_addOneNodeWithOneScope() {
        GraphWrapper wrapper = new GraphWrapper(START_SCOPE_NAME);

        wrapper.addSequence(FIRST_SEQUENCE_NODE);
        final Scope endScope = wrapper.getLastAddedScope();

        assertTrue(wrapper.isGraphConsistent());
        assertEquals(endScope, wrapper.getEndScope());
        assertEquals(START_SCOPE_NAME, wrapper.getStartScope().getName());
    }

    @Test
    void test_getSuccessors_twoNodes_returnsSecond() {
        GraphWrapper wrapper = new GraphWrapper(START_SCOPE_NAME);

        wrapper.addSequence(FIRST_SEQUENCE_NODE);

        assertTrue(wrapper.isGraphConsistent());
        final Scope firstNode = wrapper.getStartScope();
        assertEquals(wrapper.getEndScope(), wrapper.getSuccessors(firstNode).get(0));
    }

    @Test
    void test_getPredecessors_twoNodes_returnsSecond() {
        GraphWrapper wrapper = new GraphWrapper(START_SCOPE_NAME);

        wrapper.addSequence(FIRST_SEQUENCE_NODE);

        assertTrue(wrapper.isGraphConsistent());
        final Scope endNode = wrapper.getEndScope();
        assertEquals(wrapper.getStartScope(), wrapper.getPredecessors(endNode).get(0));
    }

    @Test
    void test_getEndScope_isGraphConsistent_emptyGraph_returnsTrue() {
        assertTrue(new GraphWrapper().isGraphConsistent());
    }
}