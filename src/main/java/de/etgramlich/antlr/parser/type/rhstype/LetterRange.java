package de.etgramlich.antlr.parser.type.rhstype;

import de.etgramlich.antlr.parser.type.BnfType;
import de.etgramlich.antlr.util.StringUtil;
import de.etgramlich.antlr.util.visitor.BnfElement;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class LetterRange implements BnfType, BnfElement {
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

    @Contract(pure = true)
    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        visitor.visit(this);
    }
}
