package de.etgramlich.dsl.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    void asString_withNullArgument_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> CollectionUtil.asString(null));
    }

    @Test
    void asString_withSetOnlyOneElement_returnsStringItself() {
        final String string = "String";

        final String asString = CollectionUtil.asString(Set.of(string));
        assertEquals(string, asString);
    }

    @Test
    void asString_emptySet_returnsEmptyString() {
        assertEquals(StringUtils.EMPTY, CollectionUtil.asString(Collections.emptySet()));
    }

    @Test
    void asString_setOfFiveElements_returnsAppendedStrings() {
        final Set<String> strings = Set.of("String0", "String1", "String2", "String3", "String4");

        final String result = CollectionUtil.asString(strings);

        for (String str : strings) {
            assertEquals(1, StringUtils.countMatches(result, str));
        }
        assertFalse(result.endsWith(", "));
        assertFalse(result.endsWith(" "));
    }
}