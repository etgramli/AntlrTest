package de.etgramlich.antlr.util.graph.node;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class SequenceNode extends Node {
    private final SequenceNode successor;

    public SequenceNode(final String name) {
        this(name, null);
    }

    public SequenceNode(final String name, final SequenceNode successor) {
        super(name);
        this.successor = successor;
    }

    @Contract(pure = true)
    public SequenceNode getSuccessor() {
        return successor;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SequenceNode that = (SequenceNode) o;

        if (!getName().equals(that.getName())) return false;
        return Objects.equals(successor, that.successor);
    }

    @Override
    public int hashCode() {
        final int superHashCode = super.hashCode();
        final int successorHashCode = successor != null ? successor.hashCode() : 0;
        return 31 * superHashCode + successorHashCode;
    }
}
