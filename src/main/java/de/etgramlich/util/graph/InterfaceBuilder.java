package de.etgramlich.util.graph;

import de.etgramlich.util.StringUtil;
import de.etgramlich.util.graph.type.GraphWrapper;
import de.etgramlich.util.graph.type.Scope;
import de.etgramlich.util.graph.type.ScopeEdge;
import de.etgramlich.util.graph.type.node.Node;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.Graph;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class InterfaceBuilder {
    private static final String INTERFACE_FILENAME = "src/main/resources/ebnf.stg";
    private static final String INTERFACE_NAME = "templateInterface";
    private static final String DEFAULT_FILE_ENDING = ".java";
    private static final STGroup stgroup = new STGroupFile(INTERFACE_FILENAME);

    private final String targetDirectory;
    private final String targetPackage;
    private final String packageDirectory;

    public InterfaceBuilder(final String targetDirectory, final String targetPackage) {
        this.targetDirectory = targetDirectory;
        this.targetPackage = targetPackage;
        this.packageDirectory = targetPackage.replaceAll("\\.", "/");
    }

    public void saveInterfaces(final Graph<Scope, ScopeEdge> graph) {
        final GraphWrapper graphWrapper = new GraphWrapper(graph);

        Scope scope = graphWrapper.getStartScope();
        List<Node> nodes = graphWrapper.getOutGoingNodes(scope);

        while (graphWrapper.getEndScope() != scope) {
            saveInterface(scope, nodes, graphWrapper.getSuccessors(scope).get(0));

            scope = graphWrapper.getSuccessors(scope).get(0);
            nodes = graphWrapper.getOutGoingNodes(scope);
        }
    }

    public void saveInterface(final Scope scope, final Collection<Node> nodes, final Scope nextScope) {
        // ToDo
    }

    private void saveInterface(final String name, final Collection<Method> methods) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Interface name must not be blank!");
        }
        if (!StringUtil.startsWithUpperCase(name)) {
            throw new IllegalArgumentException("Interface name must start with an upper case letter!");
        }

        final ST st = stgroup.getInstanceOf(INTERFACE_NAME);
        st.add("package", targetPackage);
        st.add("interfaceName", name);

        // Add return type and method name to String List to add to StringTemplate
        final List<String> methodList = new ArrayList<>(methods.size() * 2);
        for (Method method : methods) {
            methodList.add(method.getReturnType());
            methodList.add(method.getName());
        }
        st.add("methods", methodList);

        final String filePath = targetDirectory + packageDirectory + name + DEFAULT_FILE_ENDING;
        try (PrintWriter out = new PrintWriter(filePath)) {
            out.write(st.render());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class Method {
        private final String returnType;
        private final String name;
        private final List<Argument> arguments;

        public Method(final String returnType, final String name, final List<Argument> arguments) {
            this.returnType = returnType;
            this.name = name;
            this.arguments = List.copyOf(arguments);
        }
        public Method(final String returnType, final String name) {
            this(returnType, name, Collections.emptyList());
        }

        public String getReturnType() {
            return returnType;
        }
        public String getName() {
            return name;
        }
        public List<Argument> getArguments() {
            return arguments;
        }
    }
    private static class Argument {
        private final String type;
        private final String name;

        public Argument(final String type, final String name) {
            this.type = type;
            this.name = name;
        }
        public String getType() {
            return type;
        }
        public String getName() {
            return name;
        }
    }
}
