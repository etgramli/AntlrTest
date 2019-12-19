package de.etgramlich.semanticmodel.type;

import de.etgramlich.util.StringUtil;
import org.jetbrains.annotations.Contract;

public final class Field {
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
