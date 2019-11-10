package de.etgramlich.antlr.parser.listener.type.rhstype;

import de.etgramlich.antlr.parser.listener.type.BnfType;
import de.etgramlich.antlr.parser.listener.type.rhstype.repetition.AbstractRepetition;
import de.etgramlich.antlr.parser.listener.type.terminal.AbstractId;
import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.List;

public final class Element implements BnfType, RhsType {
    private final AbstractId id;
    private final AbstractRepetition alternatives;
    private final LetterRange range;

    @Contract(pure = true)
    public Element(final AbstractId id) {
        this.id = id;
        alternatives = null;
        range = null;
    }

    @Contract(pure = true)
    public Element(final AbstractRepetition alternatives) {
        id = null;
        this.alternatives = alternatives;
        range = null;
    }

    @Contract(pure = true)
    public Element(final LetterRange letterRange) {
        id = null;
        alternatives = null;
        range = letterRange;
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

    @Contract(pure = true)
    @Override
    public boolean isLeaf() {
        return id != null;
    }

    @Override
    public List<RhsType> getChildren() {
        if (alternatives != null) {
            return List.copyOf(alternatives.getAlternatives());
        } else {
            return Collections.emptyList();
        }
    }
}
