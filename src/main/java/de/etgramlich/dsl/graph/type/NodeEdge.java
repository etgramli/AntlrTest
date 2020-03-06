package de.etgramlich.dsl.graph.type;

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

        return node.equals(nodeEdge.node);
    }

    @Override
    public int hashCode() {
        int hashCode = getSource().hashCode();
        hashCode = 31 * hashCode + getTarget().hashCode();
        hashCode = 31 * hashCode + node.hashCode();
        return hashCode;
    }
}
