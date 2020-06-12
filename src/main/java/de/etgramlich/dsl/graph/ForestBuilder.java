package de.etgramlich.dsl.graph;

import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import de.etgramlich.dsl.graph.type.Node;
import de.etgramlich.dsl.parser.type.Bnf;
import de.etgramlich.dsl.parser.type.BnfRule;
import de.etgramlich.dsl.util.exception.InvalidGraphException;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.stream.Collectors.*;

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
        final List<BnfRule> startBnfRules = bnf.getBnfRules().stream()
                .filter(BnfRule::isStartRule)
                .collect(toUnmodifiableList());
        if (startBnfRules.size() != 1) {
            throw new IllegalArgumentException("Rule-list must have exactly one Start rule");
        }

        startRule = startBnfRules.get(0);
        forest = bnf.getBnfRules().stream()
                .map(rule -> new GraphBuilder(rule).getGraph())
                .collect(toUnmodifiableSet());

        if (hasCircularDependencies()) {
            throw new IllegalArgumentException("BNF must not have circular dependencies in its rules!");
        }

        final Set<String> ruleNames = forest.stream()
                .map(BnfRuleGraph::getName)
                .collect(toUnmodifiableSet());
        final String notDefinedNonTerminals = forest.stream()
                .flatMap(graph -> graph.getNonTerminalNodes().stream())
                .map(Node::getName).filter(Predicate.not(ruleNames::contains))
                .collect(joining(", "));
        if (!StringUtils.isBlank(notDefinedNonTerminals)) {
            throw new IllegalArgumentException("Bnf does not contain all rules to replace non-terminals: "
                    + notDefinedNonTerminals);
        }
    }

    /**
     * Returns one graph with non-terminals replaced by other graphs.
     * @return A BnfRuleGraph, not null.
     */
    public BnfRuleGraph getMergedGraph() {
        final GraphBuilder gb = new GraphBuilder(startRule);
        while (gb.getGraph().containsNonTerminals()) {
            gb.replaceNonTerminals(forest);
        }
        gb.removeSuperfluousScopes();
        final BnfRuleGraph graph = gb.getGraph();
        if (!graph.isConsistent()) {
            throw new InvalidGraphException("Graph not valid after merge!");
        }
        return graph;
    }

    /**
     * Determines whether any of the rules in the bnf have circular dependencies to each other.
     * @return True if there is at least one circular dependency.
     */
    public boolean hasCircularDependencies() {
        final UniqueStringSupplier supplier = new UniqueStringSupplier();
        final DirectedPseudograph<String, String> dependencies = new DirectedPseudograph<>(supplier, supplier, false);
        forest.forEach(rule -> dependencies.addVertex(rule.getName()));

        for (BnfRuleGraph graph : forest) {
            graph.getNonTerminalNodes().forEach(node -> dependencies.addEdge(graph.getName(), node.getName()));
        }

        return new CycleDetector<>(dependencies).detectCycles();
    }

    private static final class UniqueStringSupplier implements Supplier<String> {
        @Override
        public String get() {
            return UUID.randomUUID().toString();
        }
    }
}
