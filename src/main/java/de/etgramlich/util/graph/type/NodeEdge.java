package de.etgramlich.util.graph.type;

import de.etgramlich.util.graph.type.node.Node;

public final class NodeEdge extends ScopeEdge {
    private final Node node;

    public NodeEdge(final Scope source, final Scope target, final Node node) {
        super(source, target);
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null!");
        }
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

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
