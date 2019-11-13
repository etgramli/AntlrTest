package de.etgramlich.antlr.parser.listener.type.terminal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Text extends AbstractId {
    public Text(@NotNull final String id) {
        super(id.replaceAll("'", ""));
    }
    @Contract(pure = true)
    @Override
    public boolean isTerminal() {
        return true;
    }
}
