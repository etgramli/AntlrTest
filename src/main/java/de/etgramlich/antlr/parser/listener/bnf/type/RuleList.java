package de.etgramlich.antlr.parser.listener.bnf.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class RuleList {
    private final List<Rule> rules;

    public RuleList(final Collection<Rule> rules) {
        this.rules = new ArrayList<>(rules);
    }

    @NotNull
    @Contract(pure = true)
    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }
}
