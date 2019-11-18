package de.etgramlich.antlr.util.graph;

import de.etgramlich.antlr.util.graph.node.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.graph.DefaultEdge;

public final class BnfEdge extends DefaultEdge {
    private final Node source, target;

    public BnfEdge(@NotNull final Node source, @NotNull final Node target) {
        this.source = source;
        this.target = target;
    }

    @Contract(pure = true)
    @Override
    public Node getSource() {
        return source;
    }

    @Contract(pure = true)
    @Override
    public Node getTarget() {
        return target;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BnfEdge bnfEdge = (BnfEdge) o;

        if (!source.equals(bnfEdge.source)) return false;
        return target.equals(bnfEdge.target);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        return result;
    }
}
