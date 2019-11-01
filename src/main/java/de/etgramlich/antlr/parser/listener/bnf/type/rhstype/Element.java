package de.etgramlich.antlr.parser.listener.bnf.type.rhstype;

import de.etgramlich.antlr.parser.listener.bnf.type.BnfType;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.repetition.AbstractRepetition;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.AbstractId;
import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.List;

public final class Element implements BnfType, RhsType {
    private final AbstractId id;
    private final AbstractRepetition alternatives;

    @Contract(pure = true)
    public Element(final AbstractId id) {
        this.id = id;
        alternatives = null;
    }

    @Contract(pure = true)
    public Element(final AbstractRepetition alternatives) {
        id = null;
        this.alternatives = alternatives;
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
