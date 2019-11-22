package de.etgramlich.antlr.parser.type.terminal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Id extends AbstractId {
    public Id(String id) {
        super(id);
    }
    @Contract(pure = true)
    @Override
    public boolean isTerminal() {
        return false;
    }

    @NotNull
    @Override
    public List<String> getNonTerminalDependants() {
        return List.of(getName());
    }
}
