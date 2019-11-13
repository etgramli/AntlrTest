package de.etgramlich.antlr.util.graph;

public final class SequenceNode extends Node {
    protected SequenceNode(String name, Node successor, Node predceding) {
        super(name, successor, predceding);
    }
}
