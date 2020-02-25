package de.etgramlich.graph;

import de.etgramlich.util.StringUtil;
import de.etgramlich.graph.type.BnfRuleGraph;
import de.etgramlich.graph.type.Scope;
import de.etgramlich.graph.type.ScopeEdge;
import de.etgramlich.graph.type.Node;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class InterfaceBuilder {
    private static final String INTERFACE_FILENAME = "src/main/resources/ebnf.stg";
    private static final String INTERFACE_NAME = "templateInterface";
    private static final String DEFAULT_FILE_ENDING = ".java";
    private static final STGroup ST_GROUP = new STGroupFile(INTERFACE_FILENAME);

    private final String targetDirectory;
    private final String targetPackage;
    private final String packageDirectory;

    private BnfRuleGraph graph;


    public InterfaceBuilder(final String targetDirectory, final String targetPackage) throws IOException {
        if (StringUtils.isBlank(targetPackage)) {
            throw new IllegalArgumentException("Target package must not be blank!");
        }
        if (StringUtils.isBlank(targetDirectory)) {
            throw new IllegalArgumentException("Target directory must no be blank!");
        }
        this.targetDirectory = targetDirectory;
        this.targetPackage = targetPackage;
        this.packageDirectory = targetPackage.replaceAll("\\.", "/");

        Files.createDirectories(Paths.get(targetDirectory + '/' + packageDirectory));
    }

    public void saveInterfaces(final BnfRuleGraph graph) {
        this.graph = graph;
        final Set<String> interfaces = new HashSet<>();

        Deque<Scope> toVisitNext = new ArrayDeque<>(graph.vertexSet().size());
        toVisitNext.add(graph.getEndScope());

        Scope currentScope = toVisitNext.getFirst();
        while (graph.getStartScope() != currentScope) {
            Interface currentInterface = fromScope(currentScope);
            if (!interfaces.containsAll(currentInterface.parents)) {
                throw new NullPointerException("Not all parent interfaces found!");
            }
            saveInterface(currentInterface);
            interfaces.add(currentInterface.getName());

            toVisitNext.addAll(graph.getPredecessors(currentScope));
            currentScope = toVisitNext.removeFirst();
        }
    }

    private Interface fromScope(final Scope scope) {
        return new Interface(scope.getName(), getParents(scope), getMethods(scope));
    }

    private Set<String> getParents(final Scope scope) {
        final Set<ScopeEdge> incomingBackwardEdges = graph.incomingEdgesOf(scope).stream()
                .filter(edge -> graph.isBackwardEdge(edge))
                .collect(Collectors.toUnmodifiableSet());
        return incomingBackwardEdges.stream()
                .map(edge -> edge.getSource().getName())
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Converts a scope to Methods to be in the respective Interface.
     *
     * @param scope Scope to be converted to interface, to query its Methods.
     * @return List of Methods, not null, may be empty.
     */
    private List<Method> getMethods(final Scope scope) {
        final List<Node> outgoingNodes = graph.getOutGoingNodes(scope);
        final List<Method> methods = new ArrayList<>(outgoingNodes.size());

        String methodName;
        String returnType;
        List<Argument> arguments;
        for (Node node : outgoingNodes) {
            methodName = node.getName();
            returnType = StringUtils.EMPTY; // ToDo
            arguments = getArguments(node);

            methods.add(new Method(returnType, methodName, arguments));
        }

        return methods;
    }

    private List<Argument> getArguments(final Node scope) {
        // ToDo
        return Collections.emptyList();
    }

    /**
     * Saves a Java Interface with the given name and Methods.
     *
     * @param interfaceName Name of the interface, must not be blank.
     * @param methods       List of Methods of the Interface.
     */
    private void saveInterface(final Interface iface) {
        if (StringUtils.isBlank(iface.getName())) {
            throw new IllegalArgumentException("Interface name must not be blank!");
        }
        if (!StringUtil.startsWithUpperCase(iface.getName())) {
            throw new IllegalArgumentException("Interface name must start with an upper case letter!");
        }

        final ST st = ST_GROUP.getInstanceOf(INTERFACE_NAME);
        st.add("package", targetPackage);
        st.add("interfaceName", iface.getName());
        st.add("parents", List.copyOf(iface.getParents()));

        // Add return type and method name to String List to add to StringTemplate
        final List<String> methodList = new ArrayList<>(iface.getMethods().size() * 2);
        for (Method method : iface.getMethods()) {
            methodList.add(method.getReturnType());
            methodList.add(method.getName());
        }
        st.add("methods", methodList);

        final String filePath = targetDirectory + '/' + packageDirectory + iface.getName() + DEFAULT_FILE_ENDING;
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
            out.write(st.render());
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Could not write file!");
            e.printStackTrace();
        }
    }


    private static class Interface {
        private final String name;
        private final Set<String> parents;
        private final Set<Method> methods;

        public Interface(final String name, final Collection<String> parents, final Collection<Method> methods) {
            this.name = name;
            this.parents = Set.copyOf(parents);
            this.methods = Set.copyOf(methods);
        }

        public String getName() {
            return name;
        }

        public Collection<Method> getMethods() {
            return Set.copyOf(methods);
        }

        public Set<String> getParents() {
            return Set.copyOf(parents);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Interface that = (Interface) o;

            if (!name.equals(that.name)) return false;
            if (!parents.equals(that.parents)) return false;
            return methods.equals(that.methods);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + parents.hashCode();
            result = 31 * result + methods.hashCode();
            return result;
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

        public String getReturnType() {
            return returnType;
        }

        public String getName() {
            return name;
        }

        public List<Argument> getArguments() {
            return arguments;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Method method = (Method) o;

            if (!returnType.equals(method.returnType)) return false;
            if (!name.equals(method.name)) return false;
            return arguments.equals(method.arguments);
        }

        @Override
        public int hashCode() {
            int result = returnType.hashCode();
            result = 31 * result + name.hashCode();
            result = 31 * result + arguments.hashCode();
            return result;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Argument argument = (Argument) o;

            if (!type.equals(argument.type)) return false;
            return name.equals(argument.name);
        }

        @Override
        public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }
    }
}
