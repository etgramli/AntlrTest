package de.etgramlich.antlr.parser.listener.type.rhstype.repetition;

import de.etgramlich.antlr.parser.listener.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.type.BnfType;
import de.etgramlich.antlr.parser.listener.type.rhstype.RhsType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractRepetition implements BnfType, RhsType {
    private final List<Alternative> alternatives;

    AbstractRepetition(final Collection<Alternative> alternatives) {
        this.alternatives = List.copyOf(alternatives);
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public List<RhsType> getChildren() {
        return Collections.unmodifiableList(alternatives);
    }
}
