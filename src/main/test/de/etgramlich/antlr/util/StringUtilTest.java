package de.etgramlich.antlr.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void stripBlankLines_noBlankLines_returnsListWithSameElements() {
        final List<String> original = List.of(
                "First",
                "Second",
                "Third",
                "4",
                "Fünf"
        );

        final List<String> noDuplicates = StringUtil.stripBlankLines(original);
        assertNotSame(noDuplicates, original);
        assertEquals(original, noDuplicates);
    }
    @Test
    void stripBlankLines_onlyBlankLines_returnsEmptyList() {
        final List<String> original = List.of(
                " ",
                "",
                "\t",
                "  ",
                ""
        );

        final List<String> noDuplicates = StringUtil.stripBlankLines(original);
        assertNotSame(noDuplicates, original);
        assertEquals(Collections.emptyList(), noDuplicates);
    }

    @Test
    void trimBlankLines_noBlankLines_returnsListWithSameElements() {
        final List<String> original = List.of(
                "First",
                "Second",
                "Third",
                "4",
                "Fünf"
        );

        final List<String> noDuplicates = StringUtil.trimBlankLines(original);
        assertNotSame(noDuplicates, original);
        assertEquals(original, noDuplicates);
    }
    @Test
    void trimBlankLines_blankLinesInMiddle_returnsListWithSameElements() {
        final List<String> original = List.of(
                "First",
                "Second",
                "",
                "",
                "Fünf"
        );

        final List<String> noDuplicates = StringUtil.trimBlankLines(original);
        assertNotSame(noDuplicates, original);
        assertEquals(original, noDuplicates);
    }
    @Test
    void trimBlankLines_blankLinesAtMiddleAndEnd_removeOnlyLinesAtEnd() {
        final List<String> expected = List.of(
                "First",
                "",
                "Third"
        );
        final List<String> original = List.of(
                "First",
                "",
                "Third",
                " ",
                ""
        );

        final List<String> noDuplicates = StringUtil.trimBlankLines(original);
        assertNotSame(noDuplicates, original);
        assertEquals(expected, noDuplicates);
    }
    @Test
    void trimBlankLines_blankLinesAtBegin_returnsListWithoutBlankLines() {
        final List<String> expected = List.of(
                "Third",
                "4",
                "Fünf"
        );
        final List<String> original = List.of(
                "",
                "",
                "Third",
                "4",
                "Fünf"
        );

        final List<String> noDuplicates = StringUtil.trimBlankLines(original);
        assertNotSame(noDuplicates, original);
        assertEquals(expected, noDuplicates);
    }
    @Test
    void trimBlankLines_onlyBlankLines_returnsEmptyList() {
        final List<String> original = List.of(
                " ",
                "",
                "\t",
                "  ",
                ""
        );

        final List<String> noDuplicates = StringUtil.trimBlankLines(original);
        assertNotSame(noDuplicates, original);
        assertEquals(Collections.emptyList(), noDuplicates);
    }

    @Test
    void removeDuplicates_emptyList_returnsEmptyList() {
        final List<String> original = Collections.emptyList();

        final List<String> noDuplicates = StringUtil.removeDuplicates(original);
        assertNotSame(noDuplicates, original);
        assertEquals(original, noDuplicates);
    }
    @Test
    void removeDuplicates_hasDuplicates_returnsListWithNoDuplicateElements() {
        final List<String> expected = List.of(
                "First",
                "Second",
                "Third",
                "4",
                "Fünf"
        );
        final List<String> original = List.of(
                "First",
                "Second",
                "Second",
                "Second",
                "Third",
                "First",
                "4",
                "Fünf",
                "Third",
                "4",
                "First"
        );

        final List<String> noDuplicates = StringUtil.removeDuplicates(original);
        assertNotSame(noDuplicates, original);
        assertEquals(expected, noDuplicates);
    }
    @Test
    void removeDuplicates_hasNoDuplicates_returnsListWithSameElements() {
        final List<String> original = List.of(
                "First",
                "Second",
                "Third",
                "4",
                "Fünf"
        );

        final List<String> noDuplicates = StringUtil.removeDuplicates(original);
        assertNotSame(noDuplicates, original);
        assertEquals(original, noDuplicates);
    }

    @Test
    void isAllBlank_noBlankLines_returnsFalse() {
        final List<String> lines = List.of(
                "First line",
                " - ",
                "Ingrid Third",
                "Totoro"
        );

        assertFalse(StringUtil.isAllBlank(lines));
    }
    @Test
    void isAllBlank_notAllBlankLines_returnsFalse() {
        final List<String> lines = List.of(
                "",
                "  ",
                "Ingrid Third",
                "-"
        );

        assertFalse(StringUtil.isAllBlank(lines));
    }
    @Test
    void isAllBlank_allBlankLines_returnsTrue() {
        final List<String> lines = List.of(
                "",
                "  ",
                "",
                "\t"
        );

        assertTrue(StringUtil.isAllBlank(lines));
    }
    @Test
    void isAllBlank_allBlankLinesInclNull_returnsTrue() {
        final List<String> lines = new ArrayList<>(5);
        lines.add(StringUtil.EMPTY);
        lines.add(" ");
        lines.add(StringUtil.EMPTY);
        lines.add(null);
        lines.add("\t");

        assertTrue(StringUtil.isAllBlank(lines));
    }
}