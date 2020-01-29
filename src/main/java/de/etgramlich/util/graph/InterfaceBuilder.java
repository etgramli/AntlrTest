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
import java.util.*;

public final class InterfaceBuilder {
    private static final List<String> JAVA_TYPES = List.of("char", "int", "long", "float", "double", "String");
    private static final String INTERFACE_FILENAME = "src/main/resources/ebnf.stg";
    private static final String INTERFACE_NAME = "templateInterface";
    private static final String DEFAULT_FILE_ENDING = ".java";
    private static final STGroup stgroup = new STGroupFile(INTERFACE_FILENAME);

    private final String targetDirectory;
    private final String targetPackage;
    private final String packageDirectory;

    private GraphWrapper graphWrapper;
    private final List<String> types = new ArrayList<>(JAVA_TYPES);


    public InterfaceBuilder(final String targetDirectory, final String targetPackage) {
        if (StringUtils.isBlank(targetPackage)) {
            throw new IllegalArgumentException("Target package must not be blank!");
        }
        if (StringUtils.isBlank(targetDirectory)) {
            throw new IllegalArgumentException("Target directory must no be blank!");
        }
        this.targetDirectory = targetDirectory;
        this.targetPackage = targetPackage;
        this.packageDirectory = targetPackage.replaceAll("\\.", "/");
    }

    public void saveInterfaces(final Graph<Scope, ScopeEdge> graph) {
        graphWrapper = new GraphWrapper(graph);

        Scope scope = graphWrapper.getStartScope();
        List<Node> nodes = graphWrapper.getOutGoingNodes(scope);

        while (graphWrapper.getEndScope() != scope) {
            saveInterface(scope, nodes, graphWrapper.getSuccessors(scope).get(0));

            scope = graphWrapper.getSuccessors(scope).get(0);
            nodes = graphWrapper.getOutGoingNodes(scope);
        }
    }

    /**
     * Saves an interface for the given Scope, Nodes and following Scope.
     * @param scope Current scope (of the Interface to save).
     * @param nodes Nodes corresponding to the Scope.
     * @param nextScope The following Scope (for return type of the current Node).
     */
    public void saveInterface(final Scope scope, final Collection<Node> nodes, final Scope nextScope) {
        // ToDo
    }

    /**
     * Converts a scope to Methods to be in the respective Interface.
     * @param scope Scope to be converted to interface, to query its Methods.
     * @return List of Methods, not null, may be empty.
     */
    private List<Method> methodsFromScope(final Scope scope) {
        final List<Node> outgoingNodes = graphWrapper.getOutGoingNodes(scope);

        List<Method> methods = new ArrayList<>(outgoingNodes.size());
        // ToDo: Outgoing nodes and edges to Methods
        for (Node node : outgoingNodes) {
            //graphWrapper.ge
        }

        return methods;
    }

    /**
     * Saves a Java Interface with the given name and Methods.
     * @param name Name of the interface, must not be blank.
     * @param methods List of Methods of the Interface.
     */
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
