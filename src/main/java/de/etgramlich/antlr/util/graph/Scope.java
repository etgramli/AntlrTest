package de.etgramlich.antlr.util.graph;

import de.etgramlich.antlr.util.graph.node.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Scope {
    private final String name;
    private final List<Node> nodes;

    public Scope(final String name) {
        this(name, Collections.emptyList());
    }

    public Scope(@NotNull final String name, @NotNull final List<Node> nodes) {
        this.name = name;
        this.nodes = new ArrayList<>(nodes);
    }

    public void addNode(final Node node) {
        nodes.add(node);
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    public List<Node> getNodes() {
        return nodes;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scope scope = (Scope) o;

        if (!name.equals(scope.name)) return false;
        return nodes.equals(scope.nodes);
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode() + nodes.hashCode();
    }
}
