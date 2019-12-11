package de.etgramlich.antlr.parser.type.rhstype;

import de.etgramlich.antlr.parser.type.BnfType;
import de.etgramlich.antlr.parser.type.rhstype.repetition.AbstractRepetition;
import de.etgramlich.antlr.parser.type.terminal.AbstractId;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class Element implements BnfType {
    private final AbstractId id;
    private final AbstractRepetition repetition;
    private final LetterRange range;

    private final String name;

    @Contract(pure = true)
    public Element(@NotNull final AbstractId id) {
        this.id = id;
        repetition = null;
        range = null;

        this.name = id.getText();
    }

    @Contract(pure = true)
    public Element(@NotNull final AbstractRepetition repetition) {
        id = null;
        this.repetition = repetition;
        range = null;

        this.name = repetition.getName();
    }

    @Contract(pure = true)
    public Element(@NotNull final LetterRange letterRange) {
        id = null;
        repetition = null;
        range = letterRange;

        this.name = letterRange.getName();
    }


    public boolean isOptional() {
        return repetition != null && repetition.isOptional();
    }

    public boolean isRepetition() {
        return repetition != null && repetition.isRepetition();
    }

    public boolean isPrecedence() {
        return repetition != null && repetition.isPrecedence();
    }

    @Contract(pure = true)
    public boolean isId() {
        return id != null;
    }

    @Contract(pure = true)
    public AbstractId getId() {
        return id;
    }

    @Contract(pure = true)
    public List<Alternative> getRepetition() {
        return repetition != null ? repetition.getAlternatives() : Collections.emptyList();
    }

    @Contract(pure = true)
    public LetterRange getRange() {
        return range;
    }

    @Contract(pure = true)
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTerminal() {
        if (id != null) {           // Id can be either id of other rule or text
            return id.isTerminal();
        } else if (range != null) { // Range is always composed of (range of) terminals
            return true;
        } else if (repetition != null) {
            return repetition.isTerminal();
        } else {                    // Should never happen
            return true;
        }
    }

    @Override
    public List<String> getNonTerminalDependants() {
        if (isTerminal()) return Collections.emptyList();
        if (id != null) {
            return id.getNonTerminalDependants();
        } else if (range != null) {
            return range.getNonTerminalDependants();
        } else if (repetition != null) {
            return repetition.getNonTerminalDependants();
        } else {// Should never happen
            return Collections.emptyList();
        }
    }
}
