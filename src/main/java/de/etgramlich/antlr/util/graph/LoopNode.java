package de.etgramlich.antlr.util.graph;

public class LoopNode extends Node {
    private final Node child;

    public LoopNode(final String name, final Node successor, final  Node preceding, final Node child) {
        super(name, successor, preceding);
        this.child = child;
    }

    public Node getChild() {
        return child;
    }
}
