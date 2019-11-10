package de.etgramlich.antlr.semanticmodel.type.joi;

import de.etgramlich.antlr.util.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public final class JoiComponent {
    private final String name;
    private final List<JoiInterface> joiInterfaces;
    private final List<JoiMethod> joiMethods;
    private final List<JoiField> joiFields;

    public JoiComponent(final String name, final Collection<JoiInterface> joiInterfaces, final Collection<JoiMethod> joiMethods, final Collection<JoiField> joiFields) {
        if (StringUtil.isBlank(name)) {
            throw new IllegalArgumentException("Joi component name is blank!");
        }
        if (joiInterfaces.size() == 0) {
            throw new IllegalArgumentException("No Joi interfaces given!");
        }
        if (joiMethods.size() == 0) {
            throw new IllegalArgumentException("No Joi methods given!");
        }
        this.name = name;
        this.joiInterfaces = List.copyOf(joiInterfaces);
        this.joiMethods = List.copyOf(joiMethods);
        this.joiFields = List.copyOf(joiFields);
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @NotNull
    @Contract(pure = true)
    public List<JoiInterface> getJoiInterfaces() {
        return joiInterfaces;
    }

    @NotNull
    @Contract(pure = true)
    public List<JoiMethod> getJoiMethods() {
        return joiMethods;
    }

    @NotNull
    @Contract(pure = true)
    public List<JoiField> getJoiFields() {
        return joiFields;
    }
}
