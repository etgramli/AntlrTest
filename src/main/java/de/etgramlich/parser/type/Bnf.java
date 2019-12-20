package de.etgramlich.parser.type;

import de.etgramlich.util.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Bnf implements BnfType {
    private final List<BnfRule> bnfRules;

    public Bnf(final Collection<BnfRule> bnfRules) {
        this.bnfRules = List.copyOf(bnfRules);
    }

    @NotNull
    @Contract(pure = true)
    public List<BnfRule> getBnfRules() {
        return bnfRules;
    }

    @Override
    public String getName() {
        return bnfRules.isEmpty() ? StringUtil.EMPTY : bnfRules.get(0).getName();
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public List<String> getNonTerminalDependants() {
        return Collections.emptyList();
    }
}
