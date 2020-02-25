package de.etgramlich.graph.type;

import org.apache.commons.lang3.StringUtils;

public final class Node {
    /**
     * Identifier of the node.
     */
    private final String name;

    /**
     * Creates a new node with the given identifier.
     * @param name String, must not be blank.
     */
    public Node(final String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Node name must not be blank!");
        }
        this.name = name;
    }

    /**
     * Returns the name.
     * @return String, not null, not empty.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node node = (Node) o;

        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
