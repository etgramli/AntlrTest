package de.etgramlich.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public final class StringUtil extends StringUtils {
    private StringUtil() {}

    public static List<String> stripBlankLines(final List<String> lines) {
        return lines.stream().filter(s -> !isBlank(s)).collect(Collectors.toList());
    }

    public static List<String> trimBlankLines(final List<String> lines) {
        Optional<String> begin = lines.stream().filter(s -> !isBlank(s)).findFirst();
        Optional<String> end = Lists.reverse(lines).stream().filter(s -> !isBlank(s)).findFirst();

        if (begin.isEmpty() && end.isEmpty()) { // Only blank lines
            return Collections.emptyList();
        }
        final int beginIndex = begin.map(lines::indexOf).orElse(0);
        final int endIndex = end.map(lines::indexOf).orElseGet(() -> lines.size() - 1);
        return lines.subList(beginIndex, endIndex + 1);
    }

    public static List<String> removeDuplicates(final List<String> lines) {
        return lines.stream().distinct().collect(Collectors.toList());
    }

    public static boolean isAllBlank(final Collection<String> lines) {
        return isAllBlank(lines.toArray(new String[0]));
    }

    public static String removeAllWhiteSpaces(final String str) {
        return str.replaceAll("\\s+", "");
    }

    public static boolean isNumeric(final char c) {
        return isNumeric(String.valueOf(c));
    }

    public static boolean isAllNumeric(char... cs) {
        if (cs == null || cs.length == 0) return false;
        return toCharList(cs).stream().filter(c -> !isNumeric(c)).findAny().isEmpty();
    }

    private static List<Character> toCharList(final char... cs) {
        return new String(cs).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    }

    public static boolean startsWithUpperCase(final CharSequence charSequence) {
        if (charSequence == null || charSequence.length() == 0) {
            return false;
        }
        final char firstChar = charSequence.charAt(0);
        return firstChar >= 'A' && firstChar <= 'Z';
    }

    public static boolean startsWithLowerCase(final CharSequence charSequence) {
        if (charSequence == null || charSequence.length() == 0) {
            return false;
        }
        final char firstChar = charSequence.charAt(0);
        return firstChar >= 'a' && firstChar <= 'z';
    }
}
