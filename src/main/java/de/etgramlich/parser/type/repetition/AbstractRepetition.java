package de.etgramlich.parser.type.repetition;

import de.etgramlich.parser.type.Alternatives;
import de.etgramlich.parser.type.BnfType;
import de.etgramlich.parser.type.Element;
import de.etgramlich.util.StringUtil;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractRepetition implements Element, BnfType {
    private final Alternatives alternatives;

    AbstractRepetition(final Alternatives alternatives) {
        this.alternatives = alternatives;
    }

    public Alternatives getAlternatives() {
        return alternatives;
    }

    public abstract boolean isRepetition();

    public abstract boolean isOptional();

    public abstract boolean isPrecedence();

    public boolean hasOnlyOneAlternative() {
        return alternatives.getSequences().size() == 1;
    }

    @Override
    public boolean isTerminal() {
        return alternatives.getSequences().stream().filter(alternative -> !alternative.isTerminal()).findAny().isEmpty();
    }

    @Override
    public List<String> getNonTerminalDependants() {
        return getAlternatives().getSequences().stream().flatMap(a -> a.getNonTerminalDependants().stream()).collect(Collectors.toList());
    }

    @Override
    public int getNumberOfElements() {
        return alternatives.getNumberOfElements();
    }

    @Override
    public String getName() {
        return alternatives.getSequences().isEmpty() ? StringUtil.EMPTY : alternatives.getSequences().get(0).getName();
    }
}
