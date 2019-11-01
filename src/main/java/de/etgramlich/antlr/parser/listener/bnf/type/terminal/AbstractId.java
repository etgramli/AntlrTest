package de.etgramlich.antlr.parser.listener.bnf.type.terminal;

import de.etgramlich.antlr.parser.listener.bnf.type.BnfType;
import de.etgramlich.antlr.parser.listener.bnf.type.rhstype.RhsType;
import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.List;

public abstract class AbstractId implements BnfType, RhsType {
    private final String id;

    @Contract(pure = true)
    AbstractId(final String id) {
        this.id = id;
    }

    public String getText() {
        return id;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public List<RhsType> getChildren() {
        return Collections.emptyList();
    }
}
