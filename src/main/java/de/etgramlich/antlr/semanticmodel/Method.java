package de.etgramlich.antlr.semanticmodel;

import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.List;

public final class Method {
    private final Type returnType;
    private final String name;
    private final List<Argument> arguments;

    public Method(final Type returnType, final String name) {
        this(returnType, name, Collections.emptyList());
    }

    public Method(final Type returnType, final String name, final List<Argument> arguments) {
        this.returnType = returnType;
        this.name = name;
        this.arguments = List.copyOf(arguments);
    }

    @Contract(pure = true)
    public Type getReturnType() {
        return returnType;
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    public List<Argument> getArguments() {
        return arguments;
    }


    public static class Argument {
        private final Type type;
        private final String name;

        @Contract(pure = true)
        public Argument(final Type type, final String name) {
            this.type = type;
            this.name = name;
        }
        public Type getType() {
            return type;
        }
        public String getName() {
            return name;
        }
    }
}
