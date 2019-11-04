package de.etgramlich.antlr.semanticmodel.builder;

import de.etgramlich.antlr.semanticmodel.type.Method;
import de.etgramlich.antlr.semanticmodel.type.Type;

public class MethodBuilder {
    private Method method;
    private InterfaceBuilder interfaceBuilder;

    public MethodBuilder(InterfaceBuilder interfaceBuilder) {
        method = new Method();
        this.interfaceBuilder = interfaceBuilder;
    }

    public InterfaceBuilder returnType(String returnType) {
        method.setReturnType(returnType);
        return interfaceBuilder;
    }

    public MethodBuilder name(String name) {
        method.setName(name);
        return this;
    }

    public ArgumentBuilder argument() {
        ArgumentBuilder builder = new ArgumentBuilder(this);
        method.addArgument(builder.getArgument());
        return builder;
    }

    Method getMethod() {
        return method;
    }

    public static class ArgumentBuilder {
        private MethodBuilder methodBuilder;
        private Method.Argument argument;

        ArgumentBuilder(MethodBuilder builder) {
            methodBuilder = builder;
            this.argument = new Method.Argument();
        }
        public ArgumentBuilder setName(final String name) {
            argument.setName(name);
            return this;
        }
        public MethodBuilder setType(final String type) {
            argument.setType(new Type(type));
            return methodBuilder;
        }
        Method.Argument getArgument() {
            return argument;
        }
    }
}
