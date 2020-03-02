package de.etgramlich.parser.type;

import de.etgramlich.util.StringUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a a whole BNF (list of rules) as Java type.
 */
public final class Bnf implements BnfType {
    /**
     * List of rules of that EBNF grammar.
     */
    private final List<BnfRule> bnfRules;

    /**
     * Creates a new BNF with a copy of the provided bnf rules.
     * @param bnfRules Collection of BnfRule, must not be empty and not be null.
     */
    public Bnf(final Collection<BnfRule> bnfRules) {
        if (bnfRules == null || bnfRules.isEmpty()) {
            throw new IllegalArgumentException("BnfRules must not be null and not empty!");
        }
        this.bnfRules = List.copyOf(bnfRules);
    }

    /**
     * Retruns the immutable list of Bnf rules.
     * @return List of BnfRule, not null, not empty.
     */
    public List<BnfRule> getBnfRules() {
        return bnfRules;
    }

    @Override
    public String getName() {
        return bnfRules.isEmpty() ? StringUtil.EMPTY : bnfRules.get(0).getName();
    }

    @Override
    public int getNumberOfElements() {
        return bnfRules.size();
    }
}
