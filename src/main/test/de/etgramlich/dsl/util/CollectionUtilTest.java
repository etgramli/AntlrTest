package de.etgramlich.dsl.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilTest {

    @Test
    void hasDuplicates_emptyCollection_returnsFalse() {
        assertFalse(CollectionUtil.hasDuplicates(Collections.emptySet()));
    }

    @Test
    void hasDuplicates_null_throwsException() {
        assertThrows(NullPointerException.class, () -> CollectionUtil.hasDuplicates(null));
    }

    @Test
    void hasDuplicates_noDuplicates_returnsFalse() {
        final List<String> strings = List.of("Hello", "World", "Lorem ipsum", " dolor sit amet ", StringUtil.NEWLINE);

        assertFalse(CollectionUtil.hasDuplicates(strings));
    }

    @Test
    void hasDuplicates_duplicates_returnsTrue() {
        final List<String> strings = List.of("Hello", "World", "Lorem ipsum", " dolor sit amet ", "Hello");

        assertTrue(CollectionUtil.hasDuplicates(strings));
    }

    @Test
    void hasDuplicates_onlyDuplicates_returnsTrue() {
        final int size = 10;
        final List<String> strings = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            strings.add("Hello ");
        }

        assertTrue(CollectionUtil.hasDuplicates(strings));
    }
}