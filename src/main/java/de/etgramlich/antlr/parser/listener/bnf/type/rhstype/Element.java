package de.etgramlich.antlr.parser.listener.bnf.type.rhstype;

import de.etgramlich.antlr.parser.listener.bnf.type.BnfType;
import de.etgramlich.antlr.parser.listener.bnf.type.terminal.AbstractId;
import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.List;

public final class Element implements BnfType, RhsType {
    private final AbstractId id;
    private final List<Alternative> alternatives;

    @Contract(pure = true)
    public Element(final AbstractId id) {
        this.id = id;
        alternatives = Collections.emptyList();
    }

    @Contract(pure = true)
    public Element(final List<Alternative> alternatives) {
        id = null;
        this.alternatives = List.copyOf(alternatives);
    }


    @Contract(pure = true)
    public AbstractId getId() {
        return id;
    }

    @Contract(pure = true)
    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    @Contract(pure = true)
    @Override
    public boolean isLeaf() {
        return id != null;
    }

    @Override
    public List<RhsType> getChildren() {
        if (alternatives != null) {
            return List.copyOf(alternatives);
        } else {
            return Collections.emptyList();
        }
    }
}
