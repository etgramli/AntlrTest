package de.etgramlich.antlr.parser.type;

import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.rhstype.Element;
import de.etgramlich.antlr.parser.type.terminal.AbstractId;
import de.etgramlich.antlr.util.visitor.BnfElement;
import de.etgramlich.antlr.util.visitor.BnfTypeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;


/**
 * ToDo: Nodes in graph to Scopes
 *
 * Rhs: respect call sequence and mutual exclusive rules
 */
public final class Rule implements BnfType, BnfElement {
    private static final String INTERFACE_ST_FILENAME = "src/main/resources/ebnf.stg";

    private final AbstractId lhs;
    private final List<Alternative> rhs;

    @Contract(pure = true)
    public Rule(final AbstractId lhs, final List<Alternative> rhs) {
        this.lhs = lhs;
        this.rhs = List.copyOf(rhs);
    }

    @Contract(pure = true)
    public AbstractId getLhs() {
        return lhs;
    }

    @Contract(pure = true)
    public List<Alternative> getRhs() {
        return rhs;
    }


    @NotNull
    public String buildInterface(final String pkg) {
        final STGroup stGroup = new STGroupFile(INTERFACE_ST_FILENAME);
        ST st = stGroup.getInstanceOf("templateInterface");
        st.add("package", pkg);
        st.add("interfaceName", lhs.getText());

        final List<Method> methods = new ArrayList<>();
        Set<String> encounteredNames = new HashSet<>();
        for (Alternative alternative : rhs) {
            for (Element element : alternative.getElements()) {
                // ToDo: test for alternatives in element (is recursive)
                if (element.getId() != null && encounteredNames.add(element.getId().getText())) {
                    methods.add(new Method("int", element.getId().getText()));
                }
            }
        }
        st.add("methods", methods);

        return st.render();
    }

    @Override
    public boolean isTerminal() {
        return rhs.stream().filter(alternative -> !alternative.isTerminal()).findAny().isEmpty();
    }

    @Override
    public void accept(@NotNull BnfTypeVisitor visitor) {
        visitor.visit(this);
        rhs.forEach(alternative -> alternative.accept(visitor));
    }

    private static class Method {
        private final String returnType, name;
        private Method(final String returnType, final String name) {
            this.returnType = returnType;
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public String getReturnType() {
            return returnType;
        }
    }
}
