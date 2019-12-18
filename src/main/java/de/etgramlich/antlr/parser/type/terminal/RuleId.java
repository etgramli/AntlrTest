package de.etgramlich.antlr.parser.type.terminal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RuleId extends AbstractId {
    @Contract(pure = true)
    public RuleId(final String id) {
        super(id);
    }

    @Contract(pure = true)
    @Override
    public boolean isTerminal() {
        return true;
    }

    @NotNull
    @Override
    public List<String> getNonTerminalDependants() {
        return List.of(getName());
    }
}
