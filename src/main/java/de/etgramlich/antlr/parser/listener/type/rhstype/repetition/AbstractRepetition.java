package de.etgramlich.antlr.parser.listener.type.rhstype.repetition;

import de.etgramlich.antlr.parser.listener.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.listener.type.BnfType;
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
        for (Alternative alternative : alternatives) {
            if (!alternative.isTerminal()) {
                return false;
            }
        }
        return true;
    }
}
