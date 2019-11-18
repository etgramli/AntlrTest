package de.etgramlich.antlr.util.graph.node;

public final class SequenceNode extends Node {
    public SequenceNode(String name) {
        super(name);
    }

    public SequenceNode(String name, Node parent) {
        super(name, parent);
    }
}
