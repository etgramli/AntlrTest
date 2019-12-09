package de.etgramlich.antlr.util;

import de.etgramlich.antlr.util.graph.type.node.Node;
import de.etgramlich.antlr.util.graph.type.node.SequenceNode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilTest {

    @Test
    void toList_nullVararg_returnsEmptyList() {
        List<String> list = CollectionUtil.toList((String[]) null);

        assertNotNull(list);
        assertEquals(0, list.size());
        list.add("New");
        assertEquals(1, list.size());
    }

    @Test
    void toList_nullValue_returnsEmptyList() {
        List<String> list = CollectionUtil.toList((String) null);

        assertNotNull(list);
        assertEquals(1, list.size());
        list.add("New");
        assertEquals(2, list.size());
    }

    @Test
    void toList_oneElement_returnsListWithOneElement() {
        List<String> list = CollectionUtil.toList("Hallo Welt");

        assertEquals(1, list.size());
        assertEquals("Hallo Welt", list.get(0));
        list.add("Second");
        assertEquals(2, list.size());
    }

    @Test
    void toList_oneSequenceNode_returnsListWithOneSequenceNode() {
        final SequenceNode node = new SequenceNode("First");

        List<Node> list = CollectionUtil.toList(node);

        assertEquals(1, list.size());
        assertEquals(node, list.get(0));
        assertThrows(UnsupportedOperationException.class, () -> list.add(new SequenceNode("2nd")));
    }


    @Test
    void toList_threeSequenceNode_returnsListWithThreeSequenceNode() {
        final SequenceNode nodeThree = new SequenceNode("Third");
        final SequenceNode nodeTwo = new SequenceNode("Second", nodeThree);
        final SequenceNode nodeOne = new SequenceNode("First", nodeTwo);

        List<Node> list = CollectionUtil.toList(nodeOne);

        assertEquals(3, list.size());
        assertEquals(nodeOne, list.get(0));
        assertEquals(nodeTwo, list.get(1));
        assertEquals(nodeThree, list.get(2));
    }
}