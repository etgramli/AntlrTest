package de.etgramlich.util.graph.type;

import org.jgrapht.graph.DefaultEdge;

public abstract class ScopeEdge extends DefaultEdge {
    private Scope source;
    private Scope target;

    protected ScopeEdge(final Scope source, final Scope target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public Scope getSource() {
        return source;
    }

    public void setSource(final Scope source) {
        if (source == null) {
            throw new IllegalArgumentException("Source scope must not be null!");
        }
        this.source = source;
    }

    @Override
    public Scope getTarget() {
        return target;
    }

    public void setTarget(final Scope target) {
        if (target == null) {
            throw new IllegalArgumentException("Target scope must not be null!");
        }
        this.target = target;
    }
}
