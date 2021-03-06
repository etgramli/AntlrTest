package de.etgramlich.dsl.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.nio.CharBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Contains additional methods to operate on Strings, that are not present in the Apache Commons library.
 */
public final class StringUtil extends StringUtils {

    /**
     * Newline string for the current system.
     */
    public static final String NEWLINE = System.lineSeparator();

    /**
     * White space pattern.
     */
    private static final Pattern BLANK_CHARS_PATTERN = Pattern.compile("\\s+");

    private StringUtil() { }

    /**
     * Removes blank lines from the list.
     * @param lines List of String, must not be null.
     * @return List of String, may be empty.
     */
    public static List<String> stripBlankLines(final List<String> lines) {
        return lines.stream().filter(Predicate.not(StringUtils::isBlank)).collect(Collectors.toList());
    }

    /**
     * Removes empty Strings from the beginning and the end of the list.
     * Empty lines in the middle remain in the list.
     * @param lines List of Strings, must not be null.
     * @return List of Sting, not null, may be empty.
     */
    public static List<String> trimBlankLines(final List<String> lines) {
        final Optional<String> begin = findFirstNonBlankLine(lines);
        final Optional<String> end = findLastNonBlankLine(lines);

        if (begin.isEmpty() && end.isEmpty()) { // Only blank lines
            return Collections.emptyList();
        }
        final int beginIndex = begin.map(lines::indexOf).orElse(0);
        final int endIndex = end.map(lines::indexOf).orElseGet(() -> lines.size() - 1);
        return lines.subList(beginIndex, endIndex + 1);
    }

    private static Optional<String> findFirstNonBlankLine(final List<String> lines) {
        return lines.stream().filter(Predicate.not(StringUtils::isBlank)).findFirst();
    }

    private static Optional<String> findLastNonBlankLine(final List<String> lines) {
        return findFirstNonBlankLine(Lists.reverse(lines));
    }

    /**
     * Determines if all Strings in the collection are blank.
     * @param lines Collection of String, must not be null.
     * @return True if all Strings are blank.
     */
    public static boolean isAllBlank(final Collection<String> lines) {
        if (lines == null) {
            return true;
        }
        return lines.stream().allMatch(StringUtils::isBlank);
    }

    /**
     * Removes all whitespaces.
     * @param str String, must not be null.
     * @return String without whitespaces, may be empty.
     */
    public static String removeAllWhiteSpaces(final String str) {
        return BLANK_CHARS_PATTERN.matcher(str).replaceAll("");
    }

    /**
     * Determines if the character is numeric.
     * @param c Character
     * @return True if the char is numeric.
     */
    public static boolean isNumeric(final char c) {
        return isNumeric(String.valueOf(c));
    }

    /**
     * Determines if all characters are numeric.
     * @param cs Array of chars.
     * @return True if all chars are numeric.
     */
    public static boolean isAllNumeric(final char... cs) {
        if (cs == null || cs.length == 0) {
            return false;
        }
        return CharBuffer.wrap(cs).chars().mapToObj(c -> (char) c).allMatch(StringUtil::isNumeric);
    }

    /**
     * Determines if the first character is upper case.
     * @param charSequence Character sequence, may be null or empty.
     * @return True if the first character is a upper case letter.
     */
    public static boolean startsWithUpperCase(final CharSequence charSequence) {
        if (charSequence == null || charSequence.length() == 0) {
            return false;
        }
        final char firstChar = charSequence.charAt(0);
        return firstChar >= 'A' && firstChar <= 'Z';
    }

    /**
     * Determines if the first character is lower case.
     * @param charSequence Character sequence, may be null or empty.
     * @return True if the first character is a lower case letter.
     */
    public static boolean startsWithLowerCase(final CharSequence charSequence) {
        if (charSequence == null || charSequence.length() == 0) {
            return false;
        }
        final char firstChar = charSequence.charAt(0);
        return firstChar >= 'a' && firstChar <= 'z';
    }

    /**
     * Creates a new String with the first character being lower case.
     * @param charSequence CharSequence, must not be null, must not be blank.
     * @return New CharSequence.
     */
    public static String firstCharToLowerCase(final CharSequence charSequence) {
        if (isBlank(charSequence)) {
            throw new IllegalArgumentException("Character sequence must not be empty!");
        }
        final String firstChar = String.valueOf(Character.toLowerCase(charSequence.charAt(0)));
        return firstChar + charSequence.subSequence(1, charSequence.length());
    }

    /**
     * Creates a new String with the first character being upper case.
     * @param charSequence CharSequence, must not be blank.
     * @return New CharSequence.
     */
    public static String firstCharToUpperCase(final CharSequence charSequence) {
        if (isBlank(charSequence)) {
            throw new IllegalArgumentException("Character sequence must not be blank!");
        }
        final String firstChar = String.valueOf(Character.toUpperCase(charSequence.charAt(0)));
        return firstChar + charSequence.subSequence(1, charSequence.length());
    }
}
