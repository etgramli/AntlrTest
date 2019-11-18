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
    private final AbstractRepetition alternatives;
    private final LetterRange range;

    private final String name;

    @Contract(pure = true)
    public Element(@NotNull final AbstractId id) {
        this.id = id;
        alternatives = null;
        range = null;

        this.name = id.getText();
    }

    @Contract(pure = true)
    public Element(@NotNull final AbstractRepetition alternatives) {
        id = null;
        this.alternatives = alternatives;
        range = null;

        this.name = alternatives.getName();
    }

    @Contract(pure = true)
    public Element(@NotNull final LetterRange letterRange) {
        id = null;
        alternatives = null;
        range = letterRange;

        this.name = letterRange.getName();
    }


    public boolean isOptional() {
        return isAlternative() && alternatives.isOptional();
    }

    public boolean isRepetition() {
        return isAlternative() && alternatives.isRepetition();
    }

    public boolean isPrecedence() {
        return isAlternative() && alternatives.isPrecedence();
    }

    @Contract(pure = true)
    public boolean isId() {
        return id != null;
    }

    @Contract(pure = true)
    public boolean isAlternative() {
        return alternatives != null;
    }

    @Contract(pure = true)
    public AbstractId getId() {
        return id;
    }

    @Contract(pure = true)
    public List<Alternative> getAlternatives() {
        return alternatives != null ? alternatives.getAlternatives() : Collections.emptyList();
    }

    @Contract(pure = true)
    public LetterRange getRange() {
        return range;
    }

    @Override
    public void accept(BnfTypeVisitor visitor) {
        if (id != null) {
            id.accept(visitor);
        } else if (alternatives != null) {
            alternatives.accept(visitor);
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
        } else if (alternatives != null) {
            return alternatives.isTerminal();
        } else {                    // Should never happen
            return true;
        }
    }
}
