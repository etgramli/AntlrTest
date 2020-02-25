package de.etgramlich.graph.type;

public final class RepetitionEdge extends ScopeEdge {

    /**
     * Creates a repetition edge from source to target.
     * @param source Source vertex.
     * @param target Target vertex.
     */
    public RepetitionEdge(final Scope source, final Scope target) {
        super(source, target);
    }
}
