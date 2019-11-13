package de.etgramlich.antlr.parser.type.rhstype.repetition;

import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.BnfType;
import de.etgramlich.antlr.util.visitor.BnfElement;

import java.util.Collection;
import java.util.List;

public abstract class AbstractRepetition implements BnfType, BnfElement {
    private final List<Alternative> alternatives;

    AbstractRepetition(final Collection<Alternative> alternatives) {
        this.alternatives = List.copyOf(alternatives);
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    @Override
    public boolean isRepetition() {
        return true;
    }

    @Override
    public boolean isTerminal() {
        return alternatives.stream().filter(alternative -> !alternative.isTerminal()).findAny().isEmpty();
    }
}
