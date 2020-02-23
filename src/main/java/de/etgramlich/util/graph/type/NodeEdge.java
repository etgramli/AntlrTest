package de.etgramlich.util.graph.type;

import de.etgramlich.util.graph.type.node.Node;

import java.util.List;

public final class NodeEdge extends ScopeEdge {
    private final List<Node> nodes;

    public NodeEdge(final Scope source, final Scope target, final Node node) {
        this(source, target, List.of(node));
    }
    public NodeEdge(final Scope source, final Scope target, final List<Node> nodes) {
        super(source, target);
        this.nodes = List.copyOf(nodes);
    }


    public List<Node> getNodes() {
        return nodes;
    }

    public boolean isSequence() {
        return nodes.size() > 1;
    }

    public int getTotalNumberOfNodes() {
        int counter = nodes.size();
        for (Node node : nodes) {
            counter += node.getTotalAmountOfChildNodes();
        }
        return counter;
    }

    /**
     * Returns true if there are no nodes associated with this edge. May be empty if this edge is used for a loop or optional element.
     * @return True if at least one node is associated.
     */
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeEdge nodeEdge = (NodeEdge) o;

        return nodes.equals(nodeEdge.nodes);
    }

    @Override
    public int hashCode() {
        int hashCode = getSource().hashCode();
        hashCode = 31 * hashCode + getTarget().hashCode();
        hashCode = 31 * hashCode + nodes.hashCode();
        return hashCode;
    }
}
