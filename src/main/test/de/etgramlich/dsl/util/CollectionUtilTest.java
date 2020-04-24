package de.etgramlich.dsl.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilTest {

    @Test
    void hasDuplicates_emptyCollection_returnsFalse() {
        assertFalse(CollectionUtil.containsDuplicates(Collections.emptySet()));
    }

    @Test
    void hasDuplicates_null_throwsException() {
        assertThrows(NullPointerException.class, () -> CollectionUtil.containsDuplicates(null));
    }

    @Test
    void hasDuplicates_noDuplicates_returnsFalse() {
        final List<String> strings = List.of("Hello", "World", "Lorem ipsum", " dolor sit amet ", StringUtil.NEWLINE);

        assertFalse(CollectionUtil.containsDuplicates(strings));
    }

    @Test
    void hasDuplicates_duplicates_returnsTrue() {
        final List<String> strings = List.of("Hello", "World", "Lorem ipsum", " dolor sit amet ", "Hello");

        assertTrue(CollectionUtil.containsDuplicates(strings));
    }

    @Test
    void hasDuplicates_onlyDuplicates_returnsTrue() {
        final int size = 10;
        final List<String> strings = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            strings.add("Hello ");
        }

        assertTrue(CollectionUtil.containsDuplicates(strings));
    }

    @Test
    void toMarkdownTable_emptyMap_onlyHeadlines() {
        final String firstCol = "first";
        final String secondCol = "second";
        final String expected = firstCol + " | " + secondCol + System.lineSeparator()
                + "------|------" + System.lineSeparator();

        final String markdown = CollectionUtil.toMarkdownTable(Collections.emptyMap(), firstCol, secondCol);
        assertEquals(expected, markdown);
    }

    @Test
    void toMarkdownTable() {
        final String firstCol = "first";
        final String secondCol = "second";
        final Map<String, String> map = Map.of("A0", "B0", "A1", "B1", "A2", "B2");
        final String expected = firstCol + " | " + secondCol + System.lineSeparator() + "------|------";
        final List<String> lines = List.of("A0" + " | " + "B0" + System.lineSeparator(),
                "A1" + " | " + "B1" + System.lineSeparator(),
                "A2" + " | " + "B2" + System.lineSeparator());

        final String markdown = CollectionUtil.toMarkdownTable(map, firstCol, secondCol);
        assertTrue(markdown.startsWith(expected));
        for (String line : lines) {
            assertTrue(markdown.contains(line));
        }
    }
}