package de.etgramlich.antlr.parser.type.rhstype.repetition;

import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.BnfType;
import de.etgramlich.antlr.util.StringUtil;
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

    public abstract boolean isRepetition();

    public abstract boolean isOptional();

    public abstract boolean isPrecedence();

    public boolean hasOnlyOneAlternative() {
        return alternatives.size() == 1;
    }

    @Override
    public boolean isTerminal() {
        return alternatives.stream().filter(alternative -> !alternative.isTerminal()).findAny().isEmpty();
    }

    @Override
    public String getName() {
        return alternatives.isEmpty() ? StringUtil.EMPTY : alternatives.get(0).getName();
    }
}
