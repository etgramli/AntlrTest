package de.etgramlich.semanticmodel.builder;

import de.etgramlich.semanticmodel.scope.joi.*;
import de.etgramlich.semanticmodel.type.joi.JoiComponent;
import de.etgramlich.semanticmodel.type.joi.JoiField;
import de.etgramlich.semanticmodel.type.joi.JoiInterface;
import de.etgramlich.semanticmodel.type.joi.JoiMethod;
import de.etgramlich.util.CollectionUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class JoiBuilder {
    private String name;
    private final List<JoiInterface> joiInterfaces = new ArrayList<>();
    private final List<JoiMethod> joiMethods = new ArrayList<>();
    private final List<JoiField> joiFields = new ArrayList<>();

    private JoiBuilder() {}
    @NotNull
    @Contract(" -> new")
    static Start build() {
        return new JoiBuilder().new ComponentScopeImpl();
    }

    private class ComponentScopeImpl implements Start {
        @Override
        public InterfaceScope component(final String name) {
            JoiBuilder.this.name = name;
            return new InterfaceScopeImpl();
        }
    }
    private class InterfaceScopeImpl implements InterfaceScope {
        @Override
        public MethodScope interfaces(String name, String... names) {
            JoiBuilder.this.joiInterfaces.add(new JoiInterface(name));
            CollectionUtil.toList(names).forEach(s -> JoiBuilder.this.joiInterfaces.add(new JoiInterface(s)));
            return JoiBuilder.this.new MethodScopeImpl();
        }
    }
    private class MethodScopeImpl implements MethodScope {
        @Override
        public MoreMethodScope method(String code) {
            JoiBuilder.this.joiMethods.add(new JoiMethod(code));
            return JoiBuilder.this.new MoreMethodsScopeImpl();
        }
    }
    private class MoreMethodsScopeImpl extends FieldScopeImpl implements MoreMethodScope {
        @Override
        public MoreMethodScope method(String code) {
            JoiBuilder.this.joiMethods.add(new JoiMethod(code));
            return this;
        }
    }
    private class FieldScopeImpl extends EndScopeImpl implements FieldScope {
        @Override
        public FieldScope field(final String code) {
            JoiBuilder.this.joiFields.add(new JoiField(code));
            return this;
        }
    }
    private class EndScopeImpl implements End {
        @Override
        public JoiComponent end() {
            JoiBuilder jb = JoiBuilder.this;
            return new JoiComponent(jb.name, jb.joiInterfaces, jb.joiMethods, jb.joiFields);
        }
    }
}
