package de.etgramlich.util.graph.type.node;

import org.apache.commons.lang3.StringUtils;

public abstract class Node {
    private final String name;

    protected Node(final String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Node name must not be blank!");
        }
        this.name = name;
    }

    public abstract boolean isOptional();

    public abstract int getTotalAmountOfChildNodes();

    public String getName() {
        return name;
    }

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
