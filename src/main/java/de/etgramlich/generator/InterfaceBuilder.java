package de.etgramlich.generator;

import de.etgramlich.util.StringUtil;
import de.etgramlich.graph.type.BnfRuleGraph;
import de.etgramlich.graph.type.Scope;
import de.etgramlich.graph.type.ScopeEdge;
import de.etgramlich.graph.type.Node;
import de.etgramlich.graph.type.NodeEdge;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.stream.Collectors;

public final class InterfaceBuilder {
    /**
     * File path to the interface template.
     */
    private static final String INTERFACE_FILENAME =
            "src" + File.separator + "main" + File.separator + "resources" + File.separator + "ebnf.stg";

    /**
     * Name of the interface template.
     */
    private static final String INTERFACE_NAME = "templateInterface";

    /**
     * Java source code file ending.
     */
    private static final String DEFAULT_FILE_ENDING = ".java";

    /**
     * Loaded template file to be reused.
     */
    private static final STGroup ST_GROUP = new STGroupFile(INTERFACE_FILENAME);

    /**
     * Target directory for all packages and interfaces.
     */
    private final String targetDirectory;

    /**
     * Package to be used in interfaces.
     */
    private final String targetPackage;

    /**
     * Directory corresponding to the target package.
     */
    private final String packageDirectory;

    /**
     * Graph to build interfaces from. Private methods need reference.
     */
    private BnfRuleGraph graph;

    /**
     * Creates InterfaceBuilder with target (root) directory and package of the interfaces.
     * @param targetDirectory Target directory to contain all interfaces.
     * @param targetPackage Package of the interfaces (a subfolder will be created in target directory).
     * @throws IOException Throws exception if the target directory can not be created.
     */
    public InterfaceBuilder(final String targetDirectory, final String targetPackage) throws IOException {
        if (StringUtils.isBlank(targetPackage)) {
            throw new IllegalArgumentException("Target package must not be blank!");
        }
        if (StringUtils.isBlank(targetDirectory)) {
            throw new IllegalArgumentException("Target directory must no be blank!");
        }
        this.targetDirectory = targetDirectory;
        this.targetPackage = targetPackage;
        this.packageDirectory = targetPackage.replaceAll("\\.", File.separator);

        Files.createDirectories(Paths.get(targetDirectory + File.separator + packageDirectory));
    }

    /**
     * Save all interfaces for the scopes in the graph.
     * @param graph BnfRuleGraph, must not be null and consistent.
     */
    public void saveInterfaces(final BnfRuleGraph graph) {
        assert (graph.isConsistent());

        this.graph = graph;
        final Set<String> interfaces = new HashSet<>();

        final Set<Scope> alreadyVisited = new HashSet<>(graph.vertexSet().size());
        Deque<Scope> toVisitNext = new ArrayDeque<>(graph.vertexSet().size());
        toVisitNext.add(graph.getEndScope());

        Scope currentScope = toVisitNext.getFirst();
        while (graph.getStartScope() != currentScope) {
            Interface currentInterface = fromScope(currentScope);
            if (!interfaces.containsAll(currentInterface.getParents())) {
                throw new NullPointerException("Not all parent interfaces found!");
            }
            saveInterface(currentInterface.getName(), renderInterface(currentInterface));
            interfaces.add(currentInterface.getName());
            alreadyVisited.add(currentScope);

            toVisitNext.addAll(graph.getPredecessors(currentScope).stream()
                    .filter(scope -> !alreadyVisited.contains(scope))
                    .collect(Collectors.toUnmodifiableSet()));
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
        final Set<NodeEdge> outgoingNodes = graph.outGoingNodeEdges(scope);
        final List<Method> methods = new ArrayList<>(outgoingNodes.size());

        String methodName;
        String returnType;
        List<Argument> arguments;
        for (NodeEdge edge : outgoingNodes) {
            methodName = edge.getNode().getName();
            returnType = edge.getTarget().getName();
            arguments = getArguments(edge.getNode());

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
     * @param iface Interface to be saved, must not be null.
     * @return Interface representation as String.
     */
    private String renderInterface(final Interface iface) {
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

        return st.render();
    }

    private void saveInterface(final String interfaceName, final String javaInterface) {
        final String fullPath =
                targetDirectory + File.separator + packageDirectory + File.separator + interfaceName + DEFAULT_FILE_ENDING;
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fullPath), StandardCharsets.UTF_8)) {
            out.write(javaInterface);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Could not write file!");
            e.printStackTrace();
        }
    }
}
