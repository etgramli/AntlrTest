package de.etgramlich.graph;

import de.etgramlich.graph.type.BnfRuleGraph;
import de.etgramlich.parser.type.Bnf;
import de.etgramlich.parser.type.BnfRule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Builds a forest of BnfRuleGraphs, one for each BNF rule.
 * Can replace non-terminals in one graph by rules represented by another graph.
 */
public final class ForestBuilder {

    /**
     * Set of graphs, each representing a rule of the passed BNF.
     */
    private final Set<BnfRuleGraph> forest;

    /**
     * Start rule of the Bnf, used as root for merged graph generation.
     */
    private final BnfRule startRule;

    /**
     * Constructs a new ForestBuilder with a BnfRuleGraph for each rule.
     * @param bnf Bnf with multiple rules, must not be null.
     */
    public ForestBuilder(final Bnf bnf) {
        if (bnf == null || bnf.getBnfRules().isEmpty()) {
            throw new IllegalArgumentException("Bnf must not be null and not be empty!");
        }
        this.forest = new HashSet<>(bnf.getBnfRules().size());

        final List<BnfRule> startBnfRules = bnf.getBnfRules().stream()
                .filter(BnfRule::isStartRule)
                .collect(Collectors.toUnmodifiableList());
        if (startBnfRules.size() != 1) {
            throw new IllegalArgumentException("Rule-list must have exactly one Start rule");
        }
        startRule = startBnfRules.get(0);

        bnf.getBnfRules().forEach(rule -> forest.add(new GraphBuilder(rule).getGraph()));
    }

    /**
     * Returns one graph with non-terminals replaced by other graphs.
     * @return A BnfRuleGraph, not null.
     */
    public BnfRuleGraph getMergedGraph() {
        // ToDo
        return null;
    }
}
