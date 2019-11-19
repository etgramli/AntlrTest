package de.etgramlich.antlr.util.graph.node;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

public final class SequenceNode extends Node {
    private final SequenceNode successor;

    public SequenceNode(String name) {
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

        return Objects.equals(successor, that.successor);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (successor != null ? successor.hashCode() : 0);
        return result;
    }
}
