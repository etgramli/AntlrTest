package de.etgramlich.antlr.parser.type.terminal;

import de.etgramlich.antlr.parser.type.BnfType;
import de.etgramlich.antlr.util.StringUtil;
import de.etgramlich.antlr.util.SymbolTable;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class AbstractId implements BnfType {
    private final String id;

    @Contract(pure = true)
    AbstractId(final String id) {
        if (SymbolTable.isKeyword(id)) {
            throw new IllegalArgumentException("ID is a keyword of the host language: " + id);
        }
        if (StringUtil.isBlank(id)) {
            throw new IllegalArgumentException("ID must be non-null and not blank!");
        }
        this.id = id;
    }

    public String getText() {
        return id;
    }

    @Override
    public String getName() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
