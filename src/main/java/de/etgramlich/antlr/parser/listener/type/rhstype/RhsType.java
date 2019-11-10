package de.etgramlich.antlr.parser.listener.type.rhstype;

import java.util.List;

public interface RhsType {
    boolean isLeaf();
    List<RhsType> getChildren();
}
