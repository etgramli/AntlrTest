package de.etgramlich.antlr.semanticmodel.scope;

import de.etgramlich.antlr.semanticmodel.type.Field;
import de.etgramlich.antlr.semanticmodel.type.Interface;
import de.etgramlich.antlr.semanticmodel.type.Method;

import java.util.ArrayList;
import java.util.List;

public final class ScopeBuilder {
    private final String name;
    private final List<Interface> interfaces = new ArrayList<>();
    private final List<Method> methods = new ArrayList<>();
    private final List<Field> fields = new ArrayList<>();

    public ScopeBuilder(final String name) {
        this.name = name;
    }
}
