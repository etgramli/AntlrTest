package de.etgramlich.antlr.util.graph;

import org.jetbrains.annotations.Contract;

public abstract class Node {
    private final String name;
    private final Node successor;
    private final Node predceding;

    @Contract(pure = true)
    protected Node(final String name, final Node successor, final Node predceding) {
        this.name = name;
        this.successor = successor;
        this.predceding = predceding;
    }

    public String getName() {
        return name;
    }

    public Node getSuccessor() {
        return successor;
    }

    public Node getPredceding() {
        return predceding;
    }
}
