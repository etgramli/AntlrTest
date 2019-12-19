package de.etgramlich.semanticmodel.type.joi;

import de.etgramlich.util.StringUtil;
import org.jetbrains.annotations.Contract;

public final class JoiMethod {
    private final String code;

    @Contract(pure = true)
    public JoiMethod(final String code) {
        if (StringUtil.isBlank(code)) {
            throw new IllegalArgumentException("Code is empty!!!");
        }
        this.code = code;
    }

    @Contract(pure = true)
    public String getCode() {
        return code;
    }
}
