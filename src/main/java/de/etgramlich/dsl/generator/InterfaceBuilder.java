package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.graph.type.BnfRuleGraph;

import java.util.Set;

public interface InterfaceBuilder {

    /**
     * Generate interfaces for the scopes in the graph. There may be fewer interfaces than vertices in the graph, as
     * edges with a node of Type following an edge of Keyword will be converted to an argument of a method, and the
     * middle scope not being saved as an interface.
     *
     * @param graph BnfRuleGraph, must not be null and consistent.
     * @return Set of Interfaces representing the graph, not null, may be empty.
     */
    Set<Interface> getInterfaces(BnfRuleGraph graph);

    /**
     * Save the interfaces to files.
     * @param interfaces Set of interfaces, must not be null.
     */
    void saveInterfaces(Set<Interface> interfaces);
}
