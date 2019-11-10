package de.etgramlich.antlr.semanticmodel.type;

import de.etgramlich.antlr.util.StringUtil;
import org.jetbrains.annotations.Contract;

public final class Field {
    // ToDo: Type, Modifiers
    private final String code;

    public Field(final String code) {
        if (StringUtil.isBlank(code)) {
            throw new IllegalArgumentException("JoiField is empty!");
        }
        this.code = code;
    }

    @Contract(pure = true)
    public String getCode() {
        return code;
    }
}
