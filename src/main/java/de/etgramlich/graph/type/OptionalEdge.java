package de.etgramlich.graph.type;

public final class OptionalEdge extends ScopeEdge {

    /**
     * Creates an optional edge from source to target.
     * @param source Source vertex.
     * @param target Target vertex.
     */
    public OptionalEdge(final Scope source, final Scope target) {
        super(source, target);
    }
}
