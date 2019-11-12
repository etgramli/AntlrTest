package de.etgramlich.antlr.parser.listener.type.rhstype;

import de.etgramlich.antlr.parser.listener.type.BnfType;
import de.etgramlich.antlr.util.StringUtil;
import de.etgramlich.antlr.util.visitor.BnfElement;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class LetterRange implements BnfType, BnfElement {
    private final boolean isLetter;
    private final char beginChar, endChar;
    private final int beginInt, endInt;

    public LetterRange(boolean isLetter, char begin, char end) {
        if (!isLetter && !StringUtil.isAllNumeric(begin, end)) {
            throw new IllegalArgumentException("If isLetter is false, both chars must be digits, and were: " + begin + " and " + end);
        }
        this.isLetter = isLetter;
        if (isLetter) {
            beginChar = begin;
            endChar = end;
            beginInt = endInt = 0;
        } else {
            beginInt = Integer.parseInt(String.valueOf(begin));
            endInt = Integer.parseInt(String.valueOf(end));
            beginChar = endChar = 'A';
        }
    }

    public boolean isValid(final char c) {
        if (isLetter) {
            return c >= beginChar && c <= endChar;
        } else {
            try {
                final int value = Integer.parseInt(String.valueOf(c));
                return value >= beginInt && value <= endInt;
            } catch (NumberFormatException e) {
                return false;
            }
        }
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
