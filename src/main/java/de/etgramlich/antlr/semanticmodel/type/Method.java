package de.etgramlich.antlr.semanticmodel.type;

import de.etgramlich.antlr.util.StringUtil;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Method {
    private Type returnType;
    private String name;
    private final List<Argument> arguments;

    public Method() {
        this(new Type(StringUtil.EMPTY), StringUtil.EMPTY);
    }

    public Method(final Type returnType, final String name) {
        this(returnType, name, Collections.emptyList());
    }

    public Method(final Type returnType, final String name, final List<Argument> arguments) {
        this.returnType = returnType;
        this.name = name;
        this.arguments = new ArrayList<>(arguments);
    }

    public void setReturnType(String returnType) {
        this.returnType = new Type(returnType);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addArgument(Argument argument) {
        arguments.add(argument);
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
        private Type type;
        private  String name;

        public Argument() {
            this(new Type(StringUtil.EMPTY), StringUtil.EMPTY);
        }
        @Contract(pure = true)
        Argument(final Type type, final String name) {
            this.type = type;
            this.name = name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setType(Type type) {
            this.type = type;
        }
        public Type getType() {
            return type;
        }
        public String getName() {
            return name;
        }
    }
}
