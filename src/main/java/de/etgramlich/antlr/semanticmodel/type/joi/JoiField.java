package de.etgramlich.antlr.semanticmodel.type.joi;

import de.etgramlich.antlr.util.StringUtil;
import org.jetbrains.annotations.Contract;

public final class JoiField {
    private final String code;

    public JoiField(final String code) {
        if (StringUtil.isBlank(code)) {
            throw new IllegalArgumentException("JoiField code is empty!");
        }
        this.code = code;
    }

    @Contract(pure = true)
    public String getCode() {
        return code;
    }
}
