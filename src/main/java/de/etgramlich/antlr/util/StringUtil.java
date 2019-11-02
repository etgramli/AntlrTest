package de.etgramlich.antlr.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public final class StringUtil extends StringUtils {
    private StringUtil() {}

    public static List<String> stripBlankLines(@NotNull final List<String> lines) {
        return lines.stream().filter(s -> !isBlank(s)).collect(Collectors.toList());
    }

    @NotNull
    public static List<String> trimBlankLines(@NotNull final List<String> lines) {
        final int beginIndex = lines.indexOf(lines.stream().filter(s -> !isBlank(s)).findFirst().get());
        final int endIndex = lines.indexOf(Lists.reverse(lines).stream().filter(s -> !isBlank(s)).findFirst().get());
        return lines.subList(beginIndex, endIndex);
    }

    @NotNull
    public static List<String> removeDuplicates(@NotNull final List<String> lines) {
        return lines.stream().distinct().collect(Collectors.toList());
    }
}
