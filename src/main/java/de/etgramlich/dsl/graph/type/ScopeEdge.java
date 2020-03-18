package de.etgramlich.dsl.graph.type;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;

import java.util.Map;

/**
 * Abstract class for nodes in the BnfRuleGraph, just to make getSource() ang getTarget() more accessible.
 */
public abstract class ScopeEdge extends DefaultEdge {
    private static final long serialVersionUID = 3921976600725957520L;

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

    /**
     * Returns a Map of strings to Attribute to be drawable in a DOT graph.
     * Returns a non-empty Map with a "label" key, subclasses must also return one Attribute with the "label" key.
     * @return Unmodifiable Map with at least one entry with a "label" key.
     */
    Map<String, Attribute> getAttributeMap() {
        return Map.of("label", new DefaultAttribute<>(getClass().getSimpleName(), AttributeType.STRING));
    }
}
