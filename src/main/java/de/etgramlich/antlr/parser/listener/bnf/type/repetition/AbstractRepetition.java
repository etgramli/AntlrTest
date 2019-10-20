package de.etgramlich.antlr.parser.listener.bnf.type.repetition;

import de.etgramlich.antlr.parser.listener.bnf.type.Alternative;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractRepetition implements Repetition {
    private List<Alternative> alternatives;

    protected AbstractRepetition(final Collection<Alternative> alternatives) {
        this.alternatives = new ArrayList<>(alternatives);
    }

    @Override
    public List<Alternative> getAlternatives() {
        return Collections.unmodifiableList(alternatives);
    }
}
