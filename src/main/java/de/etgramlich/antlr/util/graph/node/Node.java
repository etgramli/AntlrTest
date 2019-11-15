package de.etgramlich.antlr.util.graph.node;

import de.etgramlich.antlr.util.StringUtil;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public abstract class Node {
    private final String name;
    private final Node parent;

    @Contract(pure = true)
    protected Node(final String name) {
        this(name, null);
    }

    @Contract(pure = true)
    protected Node(final String name, final Node parent) {
        if (StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("Name is blank!");
        }
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Node getParent() {
        return parent;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (!name.equals(node.name)) return false;
        return Objects.equals(parent, node.parent);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
