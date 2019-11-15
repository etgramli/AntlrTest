package de.etgramlich.antlr.util.graph.node;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class AlternativeNode extends Node {
    private final List<Node> children;

    public AlternativeNode(final String name, final Node parent) {
        this(name, parent, Collections.emptyList());
    }

    public AlternativeNode(final String name, final Node parent, final Collection<SequenceNode> nodes) {
        super(name, parent);
        children = new ArrayList<>(nodes);
    }

    public void addChild(final SequenceNode node) {
        children.add(node);
    }

    @NotNull
    @Contract(pure = true)
    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlternativeNode that = (AlternativeNode) o;

        if (!getName().equals(that.getName())) return false;
        if (!Objects.equals(getParent(), that.getParent())) return false;
        return children.equals(that.children);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + children.hashCode();
        return result;
    }
}
