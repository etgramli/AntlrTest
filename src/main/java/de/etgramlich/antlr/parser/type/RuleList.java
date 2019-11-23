package de.etgramlich.antlr.parser.type;

import de.etgramlich.antlr.util.StringUtil;
import de.etgramlich.antlr.util.visitor.BnfElement;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class RuleList implements BnfType, BnfElement {
    private final List<Rule> rules;

    public RuleList(final Collection<Rule> rules) {
        this.rules = List.copyOf(rules);
    }

    @NotNull
    @Contract(pure = true)
    public List<Rule> getRules() {
        return rules;
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        visitor.visit(this);
        rules.forEach(rule -> rule.accept(visitor));
    }

    @Override
    public String getName() {
        return rules.isEmpty() ? StringUtil.EMPTY : rules.get(0).getName();
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public List<String> getNonTerminalDependants() {
        return Collections.emptyList();
    }

    @Override
    public void removeNonTerminals() {
        for (Rule rule : rules) {
            rule.removeNonTerminals();
        }
    }
}
