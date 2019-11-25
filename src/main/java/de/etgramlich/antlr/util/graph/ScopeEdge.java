package de.etgramlich.antlr.util.graph;

import de.etgramlich.antlr.util.graph.node.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;

public final class ScopeEdge extends DefaultEdge {
    private final Scope source, target;
    private final List<Node> nodes;

    public ScopeEdge(@NotNull final Scope source, @NotNull final Scope target, @NotNull final Node node) {
        this(source, target, List.of(node));
    }
    public ScopeEdge(@NotNull final Scope source, @NotNull final Scope target, @NotNull final List<Node> nodes) {
        if (nodes.isEmpty()) throw new IllegalArgumentException("List of nodes must not be null!");
        this.source = source;
        this.target = target;
        this.nodes = List.copyOf(nodes);
    }

    @Contract(pure = true)
    @Override
    public Scope getSource() {
        return source;
    }

    @Contract(pure = true)
    @Override
    public Scope getTarget() {
        return target;
    }

    @Contract(pure = true)
    public List<Node> getNodes() {
        return nodes;
    }

    @Contract(pure = true)
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
    @Contract(pure = true)
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScopeEdge scopeEdge = (ScopeEdge) o;

        if (!source.equals(scopeEdge.source)) return false;
        if (!target.equals(scopeEdge.target)) return false;
        return nodes.equals(scopeEdge.nodes);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + nodes.hashCode();
        return result;
    }
}
