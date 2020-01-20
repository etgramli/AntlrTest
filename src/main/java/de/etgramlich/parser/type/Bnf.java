package de.etgramlich.parser.type;

import de.etgramlich.util.StringUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Bnf implements BnfType {
    private final List<BnfRule> bnfRules;

    public Bnf(final Collection<BnfRule> bnfRules) {
        this.bnfRules = List.copyOf(bnfRules);
    }

    public List<BnfRule> getBnfRules() {
        return bnfRules;
    }

    @Override
    public String getName() {
        return bnfRules.isEmpty() ? StringUtil.EMPTY : bnfRules.get(0).getName();
    }

    @Override
    public List<String> getNonTerminalDependants() {
        return Collections.emptyList();
    }
}
