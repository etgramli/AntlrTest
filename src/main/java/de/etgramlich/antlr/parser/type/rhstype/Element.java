package de.etgramlich.antlr.parser.type.rhstype;

import de.etgramlich.antlr.parser.type.BnfType;
import de.etgramlich.antlr.parser.type.rhstype.repetition.AbstractRepetition;
import de.etgramlich.antlr.parser.type.terminal.AbstractId;
import de.etgramlich.antlr.util.visitor.BnfElement;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class Element implements BnfType, BnfElement {
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

    @Override
    public void accept(BnfTypeVisitor visitor) {
        if (id != null) {
            id.accept(visitor);
        } else if (repetition != null) {
            repetition.accept(visitor);
        } else if (range != null) {
            range.accept(visitor);
        }
        visitor.visit(this);
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
}
