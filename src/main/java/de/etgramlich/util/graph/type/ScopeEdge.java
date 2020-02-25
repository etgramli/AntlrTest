package de.etgramlich.util.graph.type;

import org.jgrapht.graph.DefaultEdge;

public abstract class ScopeEdge extends DefaultEdge {
    /**
     * Source vertex of the edge.
     */
    private Scope source;

    /**
     * Target vertex of the edge.
     */
    private Scope target;

    protected ScopeEdge(final Scope source, final Scope target) {
        this.source = source;
        this.target = target;
    }

    /**
     * Returns the source vertex of the edge..
     * @return Scope, not null.
     */
    @Override
    public final Scope getSource() {
        return source;
    }

    /**
     * Sets a new source vertex to the edge.
     * @param source Source scope, must not be null.
     */
    public final void setSource(final Scope source) {
        if (source == null) {
            throw new IllegalArgumentException("Source scope must not be null!");
        }
        this.source = source;
    }

    /**
     * Returns the target vertex of the specified edge.
     * @return Scope, not null.
     */
    @Override
    public final Scope getTarget() {
        return target;
    }

    /**
     * Sets a new target vertex. Used to merge alternatives.
     * @param target Target scope, must not be null.
     */
    public final void setTarget(final Scope target) {
        if (target == null) {
            throw new IllegalArgumentException("Target scope must not be null!");
        }
        this.target = target;
    }
}
