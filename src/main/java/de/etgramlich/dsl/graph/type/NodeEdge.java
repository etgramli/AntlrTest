package de.etgramlich.dsl.graph.type;

import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;

import java.util.Map;

/**
 * An edge containing a node bearing the information to create an Interface from.
 */
public final class NodeEdge extends ScopeEdge {
    private static final long serialVersionUID = 7304313931535543410L;

    /**
     * Node containing information about the BNF element represented by the node.
     */
    private final Node node;

    /**
     * Creates a node edge from source to target containing the given node.
     * @param node Node, must not be null.
     */
    public NodeEdge(final Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null!");
        }
        this.node = node;
    }

    /**
     * Returns the node representing a BNF element.
     * @return Node, not null.
     */
    public Node getNode() {
        return node;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NodeEdge nodeEdge = (NodeEdge) o;

        if (getSource() != nodeEdge.getSource()) {
            return false;
        }
        if (getTarget() != nodeEdge.getTarget()) {
            return false;
        }

        return node.equals(nodeEdge.node);
    }

    @Override
    public int hashCode() {
        int hashCode = getSource() != null ? getSource().hashCode() : 0;
        hashCode = 31 * hashCode + (getTarget() != null ? getTarget().hashCode() : 0);
        hashCode = 31 * hashCode + node.hashCode();
        return hashCode;
    }

    @Override
    public String toString() {
        return "NodeEdge(" + node.toString() + ");";
    }

    @Override
    Map<String, Attribute> getAttributeMap() {
        final String labelValue = node.getName() + " [" + node.getType().toString() + "]";
        return Map.of("label", new DefaultAttribute<>(labelValue, AttributeType.STRING));
    }
}
