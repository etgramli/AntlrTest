package de.etgramlich.graph.type;

import org.jgrapht.graph.DefaultEdge;

public abstract class ScopeEdge extends DefaultEdge {
    /**
     * Returns the source vertex of the edge..
     * @return Scope, not null.
     */
    @Override
    public final Scope getSource() {
        return (Scope) super.getSource();
    }

    /**
     * Returns the target vertex of the specified edge.
     * @return Scope, not null.
     */
    @Override
    public final Scope getTarget() {
        return (Scope) super.getTarget();
    }
}
