package de.etgramlich.antlr.util.graph.node;

import de.etgramlich.antlr.util.StringUtil;
import org.jetbrains.annotations.Contract;

public abstract class Node {
    private final String name;

    @Contract(pure = true)
    protected Node(final String name) {
        if (StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("Name is blank!");
        }
        this.name = name;
    }

    public abstract int getTotalAmountOfChildNodes();

    public String getName() {
        return name;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
