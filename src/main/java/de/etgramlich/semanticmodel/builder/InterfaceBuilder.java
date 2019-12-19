package de.etgramlich.semanticmodel.builder;

import de.etgramlich.semanticmodel.type.Interface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class InterfaceBuilder {
    private Interface anInterface;

    public InterfaceBuilder(final String interfaceName) {
        anInterface = new Interface(interfaceName);
    }

    @NotNull
    @Contract("_ -> new")
    public static InterfaceBuilder Interface(final String interfaceName) {
        return new InterfaceBuilder(interfaceName);
    }

    @NotNull
    public MethodBuilder method() {
        MethodBuilder builder = new MethodBuilder(this);

        anInterface.addMethod(builder.getMethod());

        return builder;
    }

    @Contract(pure = true)
    public Interface build() {
        return anInterface;
    }
}
