package de.etgramlich.antlr.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public final class StringUtil extends StringUtils {
    private StringUtil() {}

    public static List<String> stripBlankLines(@NotNull final List<String> lines) {
        return lines.stream().filter(s -> !isBlank(s)).collect(Collectors.toList());
    }

    @NotNull
    public static List<String> trimBlankLines(@NotNull final List<String> lines) {
        Optional<String> begin = lines.stream().filter(s -> !isBlank(s)).findFirst();
        Optional<String> end = Lists.reverse(lines).stream().filter(s -> !isBlank(s)).findFirst();

        if (begin.isEmpty() && end.isEmpty()) { // Only blank lines
            return Collections.emptyList();
        }
        final int beginIndex = lines.indexOf(begin.get());
        final int endIndex = lines.indexOf(end.get());
        return lines.subList(beginIndex, endIndex+1);
    }

    @NotNull
    public static List<String> removeDuplicates(@NotNull final List<String> lines) {
        return lines.stream().distinct().collect(Collectors.toList());
    }

    @Contract(pure = true)
    public static boolean isAllBlank(@NotNull final Collection<String> lines) {
        return isAllBlank(lines.toArray(new String[0]));
    }
}
