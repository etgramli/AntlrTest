package de.etgramlich.parser.type.rhstype;

import de.etgramlich.parser.type.BnfType;
import de.etgramlich.util.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class LetterRange implements BnfType {
    private final boolean isLetter;
    private final char beginChar, endChar;

    public LetterRange(boolean isLetter, char begin, char end) {
        if (!isLetter && !StringUtil.isAllNumeric(begin, end)) {
            throw new IllegalArgumentException("If isLetter is false, both chars must be digits, and were: " + begin + " and " + end);
        }
        this.isLetter = isLetter;
        beginChar = begin;
        endChar = end;
    }

    @Contract(pure = true)
    public boolean isInRange(final char c) {
        return c >= beginChar && c <= endChar;
    }

    @NotNull
    @Contract(pure = true)
    public String getJavaType() {
        return isLetter ? "String" : "Integer";
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String getName() {
        return "(" + beginChar + ".." + endChar + ")";
    }

    @Contract(pure = true)
    @Override
    public boolean isTerminal() {
        return true;
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public List<String> getNonTerminalDependants() {
        return Collections.emptyList();
    }
}
