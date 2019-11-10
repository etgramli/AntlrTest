package de.etgramlich.antlr.semanticmodel.type.joi;

import de.etgramlich.antlr.util.StringUtil;
import org.jetbrains.annotations.Contract;

public final class JoiInterface {
    private final String name;

    public JoiInterface(final String name) {
        if (StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("Interface name is blank!");
        }
        this.name = name;
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }
}
