package de.etgramlich.parser.type.rhstype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LetterRangeTest {
    private static final LetterRange A_TO_Z_RANGE = new LetterRange(true, 'A', 'Z');
    private static final LetterRange a_TO_z_RANGE = new LetterRange(true, 'a', 'z');
    private static final LetterRange DIGIT_RANGE = new LetterRange(false, '0', '9');

    @Test
    void isInRange_AtoZ_leftBoundIsInRange() {
        assertTrue(A_TO_Z_RANGE.isInRange('A'));
    }

    @Test
    void isInRange_AtoZ_CisInRange() {
        assertTrue(A_TO_Z_RANGE.isInRange('C'));
    }

    @Test
    void isInRange_AtoZ_lowercaseCharNotInRange() {
        assertFalse(A_TO_Z_RANGE.isInRange('b'));
    }

    @Test
    void isInRange_aToz_uppercaseCharacterNotInRange() {
        assertFalse(a_TO_z_RANGE.isInRange('C'));
    }

    @Test
    void isInRange_digitRange_numberIsInRange() {
        assertTrue(DIGIT_RANGE.isInRange('0'));
        assertTrue(DIGIT_RANGE.isInRange('5'));
        assertTrue(DIGIT_RANGE.isInRange('9'));
    }

    @Test
    void isInRange_digitRagnge_characterIsNotInRange() {
        assertFalse(DIGIT_RANGE.isInRange('c'));
    }
}