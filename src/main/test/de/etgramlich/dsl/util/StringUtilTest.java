package de.etgramlich.dsl.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void isAllNumeric_noValue_returnsFalse() {
        assertFalse(StringUtil.isAllNumeric());
    }

    @Test
    void isAllNumeric_null_returnsFalse() {
        assertFalse(StringUtil.isAllNumeric((char[]) null));
    }

    @Test
    void isAllNumeric_zero_returnsTrue() {
        assertTrue(StringUtil.isAllNumeric('0'));
    }

    @Test
    void isAllNumeric_zeroToTwelve_returnsTrue() {
        assertTrue(StringUtil.isAllNumeric('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    }

    @Test
    void isAllNumeric_zeroToTwelveAndA_returnsFalse() {
        assertFalse(StringUtil.isAllNumeric('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A'));
    }

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

    @Test
    void startsWithUpperCase_allUpperCase_returnsTrue() {
        final String string = "ABC";

        assertTrue(StringUtil.startsWithUpperCase(string));
    }

    @Test
    void startsWithUpperCase_allLowerCase_returnsFalse() {
        final String string = "abc";

        assertFalse(StringUtil.startsWithUpperCase(string));
    }

    @Test
    void startsWithUpperCase_firstUpperCase_returnsTrue() {
        final String string = "Abc";

        assertTrue(StringUtil.startsWithUpperCase(string));
    }

    @Test
    void startsWithUpperCase_firstLowerCase_returnsFalse() {
        final String string = "aBC";

        assertFalse(StringUtil.startsWithUpperCase(string));
    }

    @Test
    void startsWithUpperCase_null_returnsFalse() {
        final String string = null;

        assertFalse(StringUtil.startsWithUpperCase(string));
    }

    @Test
    void startsWithUpperCase_emptyString_returnsFalse() {
        final String string = "";

        assertFalse(StringUtil.startsWithUpperCase(string));
    }

    @Test
    void startsWithUpperCase_blankString_returnsFalse() {
        final String string = "  \t";

        assertFalse(StringUtil.startsWithUpperCase(string));
    }

    @Test
    void startsWithLowerCase_allUpperCase_returnsFalse() {
        final String string = "ABC";

        assertFalse(StringUtil.startsWithLowerCase(string));
    }

    @Test
    void startsWithLowerCase_allLowerCase_returnsTrue() {
        final String string = "abc";

        assertTrue(StringUtil.startsWithLowerCase(string));
    }

    @Test
    void startsWithLowerCase_firstUpperCase_returnsFalse() {
        final String string = "Abc";

        assertFalse(StringUtil.startsWithLowerCase(string));
    }

    @Test
    void startsWithLowerCase_firstLowerCase_returnsTrue() {
        final String string = "aBC";

        assertTrue(StringUtil.startsWithLowerCase(string));
    }

    @Test
    void startsWithLowerCase_null_returnsFalse() {
        final String string = null;

        assertFalse(StringUtil.startsWithLowerCase(string));
    }

    @Test
    void startsWithLowerCase_emptyString_returnsFalse() {
        final String string = "";

        assertFalse(StringUtil.startsWithLowerCase(string));
    }

    @Test
    void startsWithLowerCase_blankString_returnsFalse() {
        final String string = "  \t";

        assertFalse(StringUtil.startsWithLowerCase(string));
    }
}
