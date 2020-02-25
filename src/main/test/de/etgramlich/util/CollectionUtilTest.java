package de.etgramlich.util;

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
}