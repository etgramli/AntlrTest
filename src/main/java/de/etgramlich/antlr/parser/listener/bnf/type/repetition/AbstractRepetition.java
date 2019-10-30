package de.etgramlich.antlr.parser.listener.bnf.type.repetition;

import de.etgramlich.antlr.parser.listener.bnf.type.Alternative;
import de.etgramlich.antlr.parser.listener.bnf.type.BnfType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractRepetition implements BnfType {
    private final List<Alternative> alternatives;

    AbstractRepetition(final Collection<Alternative> alternatives) {
        this.alternatives = List.copyOf(alternatives);
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }
}
