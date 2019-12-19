package de.etgramlich.parser.type.terminal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class Text extends AbstractId {
    public Text(@NotNull final String id) {
        super(id.replaceAll("'", ""));
    }

    @Contract(pure = true)
    @Override
    public boolean isTerminal() {
        return true;
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public List<String> getNonTerminalDependants() {
        return Collections.emptyList();
    }
}
