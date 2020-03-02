package de.etgramlich.graph.type;

import org.jgrapht.graph.DefaultEdge;

/**
 * Abstract class for nodes in the BnfRuleGraph, just to make getSource() ang getTarget() more accessible.
 */
public abstract class ScopeEdge extends DefaultEdge {

    /**
     * Returns the source vertex of the edge.
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
