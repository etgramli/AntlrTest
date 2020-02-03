package de.etgramlich.parser.type;

import de.etgramlich.parser.type.text.NonTerminal;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Rhs: respect call sequence and mutual exclusive rules
 */
public final class BnfRule implements BnfType {
    private final NonTerminal lhs;
    private final Alternatives rhs;

    public BnfRule(final NonTerminal lhs, final Alternatives rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public NonTerminal getLhs() {
        return lhs;
    }

    public Alternatives getRhs() {
        return rhs;
    }

    public boolean hasOnlyOneAlternative() {
        return rhs.getSequences().size() == 1;
    }

    @Override
    public String getName() {
        return lhs.getName();
    }

    @Override
    public boolean isTerminal() {
        return rhs.getSequences().stream().filter(alternative -> !alternative.isTerminal()).findAny().isEmpty();
    }

    public boolean isStartRule() {
        return !isTerminal() && hasOnlyOneAlternative()
                && rhs.getElements().size() == 1 && rhs.getElements().get(0) instanceof NonTerminal;
    }

    @Override
    public int getNumberOfElements() {
        return rhs.getNumberOfElements();
    }

    @Override
    public List<String> getNonTerminalDependants() {
        if (isTerminal()) return Collections.emptyList();
        return rhs.getSequences().stream().
                flatMap(sequence -> sequence.getNonTerminalDependants().stream()).collect(Collectors.toList());
    }
}
