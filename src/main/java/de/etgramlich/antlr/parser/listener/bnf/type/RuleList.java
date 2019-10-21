package de.etgramlich.antlr.parser.listener.bnf.type;

import de.etgramlich.antlr.util.BnfElement;
import de.etgramlich.antlr.util.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public final class RuleList implements BnfElement {
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
        rules.forEach(rule -> rule.accept(visitor));
        visitor.visit(this);
    }
}
