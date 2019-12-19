package de.etgramlich.semanticmodel.builder;

import de.etgramlich.semanticmodel.type.Type;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class TypeBuilder {
    private Type type;


    @NotNull
    @Contract(value = " -> new", pure = true)
    public static TypeBuilder getTypeBuilder() {
        return new TypeBuilder();
    }

    public void setTypeName(final String typeName) {
        type = new Type(typeName);
    }

    @Contract(pure = true)
    public Type getType() {
        return type;
    }
}
