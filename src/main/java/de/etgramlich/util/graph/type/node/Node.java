package de.etgramlich.util.graph.type.node;

import de.etgramlich.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Node {
    private final String name;
    private Node successor;

    protected Node(final String name) {
        this(name, null);
    }

    protected Node(final String name, final Node successor) {
        if (StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("Name is blank!");
        }
        this.name = name;
        this.successor = successor;
    }

    public abstract boolean isOptional();

    public abstract int getTotalAmountOfChildNodes();

    public String getName() {
        return name;
    }

    public Node getSuccessor() {
        return successor;
    }

    public void setSuccessor(@NotNull final Node successor) {
        this.successor = successor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (!name.equals(node.name)) return false;
        return Objects.equals(successor, node.successor);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (successor != null ? successor.hashCode() : 0);
        return result;
    }
}
