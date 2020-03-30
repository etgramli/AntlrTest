package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import de.etgramlich.dsl.graph.type.Node;
import de.etgramlich.dsl.graph.type.NodeEdge;
import de.etgramlich.dsl.graph.type.NodeType;
import de.etgramlich.dsl.graph.type.OptionalEdge;
import de.etgramlich.dsl.graph.type.Scope;
import de.etgramlich.dsl.graph.type.ScopeEdge;
import de.etgramlich.dsl.util.StringUtil;
import de.etgramlich.dsl.util.SymbolTable;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Creates Java interfaces from a BnfRuleGraph and saves them to files.
 */
public final class InterfaceBuilder {
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
    private static final STGroup ST_GROUP;

    static {
        final URL templateFileUrl = Thread.currentThread().getContextClassLoader().getResource("ebnf.stg");
        if (templateFileUrl == null) {
            throw new RuntimeException("Template fie ebnf.stg could not be read!");
        }
        ST_GROUP = new STGroupFile(templateFileUrl);
    }

    /**
     * Package to be used in interfaces.
     */
    private final String targetPackage;

    /**
     * Directory corresponding to the target package.
     */
    private final String packageDirectory;

    /**
     * Saves already saved interfaces, contains mapping from grammar keywords to java keywords.
     */
    private final SymbolTable symbolTable;

    /**
     * Creates InterfaceBuilder with a target (root) directory and package of the interfaces.
     *
     * @param targetDirectory Target directory to contain all interfaces.
     * @param targetPackage   Package of the interfaces (a subfolder will be created in target directory).
     */
    public InterfaceBuilder(final String targetDirectory, final String targetPackage) {
        if (StringUtils.isBlank(targetPackage)) {
            throw new IllegalArgumentException("Target package must not be blank!");
        }
        if (StringUtils.isBlank(targetDirectory)) {
            throw new IllegalArgumentException("Target directory must not be blank!");
        }
        this.targetPackage = targetPackage;
        packageDirectory = targetDirectory + File.separator + targetPackage.replace('.', File.separatorChar);
        symbolTable = new SymbolTable();
    }

    /**
     * Generate interfaces for the scopes in the graph. There may be fewer interfaces than vertices in the graph, as
     * edges with a node of Type following an edge of Keyword will be converted to an argument of a method, and the
     * middle scope not being saved as an interface.
     *
     * @param graph BnfRuleGraph, must not be null and consistent.
     * @return Set of Interfaces representing the graph, not null, may be empty.
     */
    public Set<Interface> getInterfaces(final BnfRuleGraph graph) {
        if (!graph.isConsistent()) {
            throw new IllegalArgumentException("Graph is not consistent!");
        }

        final Deque<Scope> toVisitNext = new ArrayDeque<>(graph.vertexSet().size());
        final Set<Interface> interfaces = new HashSet<>(graph.vertexSet().size());

        Scope currentScope = graph.getEndScope();
        while (currentScope != null) {
            final Interface currentInterface = getInterface(currentScope, graph);
            interfaces.add(currentInterface);
            symbolTable.addType(currentInterface.getName());

            graph.getPrecedingKeywords(currentScope).stream()
                    .filter(scope -> !symbolTable.isType(scope.getName()))
                    .forEach(toVisitNext::add);
            graph.incomingEdgesOf(currentScope).stream()
                    .filter(edge -> edge instanceof OptionalEdge)
                    .map(ScopeEdge::getSource)
                    .forEach(toVisitNext::add);
            currentScope = toVisitNext.pollFirst();
        }

        final Set<String> unsavedTypes = typesToBeSaved(interfaces).stream()
                .filter(type -> !symbolTable.isType(type))
                .collect(Collectors.toUnmodifiableSet());
        if (!unsavedTypes.isEmpty()) {
            throw new IllegalArgumentException("Not all scopes saved as interfaces: "
                    + String.join(", ", unsavedTypes));
        }
        return interfaces;
    }

    /**
     * Generate a set of types that occur in the set of interfaces as interface itself or as method argument type.
     * @param interfaces Set of interfaces, must not be null.
     * @return Set of string, not null.
     */
    private Set<String> typesToBeSaved(final Set<Interface> interfaces) {
        final Set<String> types = new HashSet<>(interfaces.size());
        for (Interface anInterface : interfaces) {
            types.add(anInterface.getName());
            types.addAll(anInterface.getParents());
            for (Method method : anInterface.getMethods()) {
                types.add(method.getReturnType());
                types.addAll(method.getArguments().stream().map(Argument::getType).collect(Collectors.toList()));
            }
        }
        return Collections.unmodifiableSet(types);
    }

    private Interface getInterface(final Scope currentScope, final BnfRuleGraph graph) {
        return new Interface(
                currentScope.getName(),
                getParents(currentScope, graph).stream().map(Scope::getName).collect(Collectors.toUnmodifiableSet()),
                getMethods(currentScope, graph));
    }

    private Set<Scope> getParents(final Scope currentScope, final BnfRuleGraph graph) {
        return graph.outgoingEdgesOf(currentScope).stream()
                .filter(edge -> edge instanceof OptionalEdge)
                .map(ScopeEdge::getTarget)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Generate the methods associated to an interface for the given scope.
     *
     * @param scope Scope to be converted to interface, to query its Methods.
     * @param graph Graph with the EBNF representation.
     * @return List of Methods, not null, may be empty.
     */
    private Set<Method> getMethods(final Scope scope, final BnfRuleGraph graph) {
        return graph.outgoingEdgesOf(scope).stream()
                .filter(edge -> edge instanceof NodeEdge)
                .map(edge -> (NodeEdge) edge)
                .filter(edge -> edge.getNode().getType().equals(NodeType.KEYWORD))
                .flatMap(edge -> methodsFromNodeEdge(edge, graph).stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Generate methods from a NodeEdge.
     * @param edge NodeEdge, must not be null, must exist in the graph.
     * @param graph BnfRuleGraph, must not be null, must contain edge.
     * @return Set of Method, not null, not empty.
     */
    private Set<Method> methodsFromNodeEdge(final NodeEdge edge, final BnfRuleGraph graph) {
        final Set<Scope> subsequent = graph.getSubsequentType(edge.getTarget());
        if (subsequent.isEmpty()) {
            return Set.of(new Method(edge.getTarget().getName(), edge.getNode().getName(), Collections.emptyList()));
        } else {
            return subsequent.stream()
                    .map(scope -> new Method(scope.getName(), edge.getNode().getName(), getArgument(edge, graph)))
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    /**
     * Generate an Argument from a NodeEdge.
     * @param nodeEdge NodeEdge, must not be null, must exist in the graph.
     * @param graph BnfRuleGraph, must not be null, must contain edge.
     * @return New Argument object.
     */
    private Argument getArgument(final NodeEdge nodeEdge, final BnfRuleGraph graph) {
        if (nodeEdge == null) {
            throw new IllegalArgumentException("NodeEdge must not be null!");
        }
        if (!nodeEdge.getNode().getType().equals(NodeType.KEYWORD)) {
            throw new IllegalArgumentException("Preceding node type must be KEYWORD!");
        }
        if (graph.getOutGoingNodes(nodeEdge.getTarget()).size() > 1) {
            throw new IllegalArgumentException("Type node must be followed by exactly one Type node edge!");
        }
        final Node follower = graph.getOutGoingNodes(nodeEdge.getTarget()).iterator().next();
        if (!follower.getType().equals(NodeType.TYPE)) {
            throw new IllegalArgumentException("Type of preceding node is not Type, but was: "
                    + follower.getType().toString());
        }
        return new Argument(follower.getName(), StringUtil.firstCharToLowerCase(follower.getName()));
    }

    /**
     * Render the passed interface as a Java Interface.
     *
     * @param anInterface Interface to be saved, must not be null.
     * @return Interface representation as String.
     */
    String renderInterface(final Interface anInterface) {
        if (StringUtils.isBlank(anInterface.getName())) {
            throw new IllegalArgumentException("Interface name must not be blank!");
        }
        if (!StringUtil.startsWithUpperCase(anInterface.getName())) {
            throw new IllegalArgumentException("Interface name must start with an upper case letter!");
        }

        final ST st = ST_GROUP.getInstanceOf(INTERFACE_NAME);
        st.add("package", targetPackage);
        st.add("interfaceName", anInterface.getName());
        st.add("methods", anInterface.getMethods());

        final List<String> parents = List.copyOf(anInterface.getParents());
        if (!parents.isEmpty()) {
            st.add("firstParent", parents.get(0));
            if (parents.size() > 1) {
                st.add("parents", parents.subList(1, parents.size()));
            }
        }

        return st.render();
    }

    /**
     * Generate a set of strings containing the names of the interface to be saved of the passed graph.
     * @param graph BnfRuleGraph, must not be null.
     * @return Set of String, not null, may be empty.
     */
    Set<String> getInterfacesToSave(final BnfRuleGraph graph) {
        final Set<String> interfaces = new HashSet<>(Set.of(graph.getStartScope().getName()));

        final Set<Scope> toVisitNext = Collections.synchronizedSet(new HashSet<>());
        Scope currentScope = graph.getStartScope();
        while (currentScope != null) {
            Stream.of(graph.outGoingNodeEdges(currentScope).stream()
                    .filter(edge -> edge.getNode().getType().equals(NodeType.KEYWORD))
                    .map(ScopeEdge::getTarget)
                    .flatMap(scope -> graph.outGoingNodeEdges(scope).stream())
                    .map(edge -> edge.getNode().getType().equals(NodeType.TYPE) ? edge.getTarget() : edge.getSource()),
                getParents(currentScope, graph).stream()).flatMap(stream -> stream)
                    .filter(scope -> !interfaces.contains(scope.getName()))
                    .forEach(toVisitNext::add);

            if (!toVisitNext.isEmpty()) {
                currentScope = toVisitNext.iterator().next();
                toVisitNext.remove(currentScope);
                interfaces.add(currentScope.getName());
            } else {
                currentScope = null;
            }
        }
        return Collections.unmodifiableSet(interfaces);
    }

    /**
     * Save the interfaces to files.
     * @param interfaces Set of interfaces, must not be null.
     */
    public void saveInterfaces(final Set<Interface> interfaces) {
        interfaces.forEach(anInterface -> saveInterface(anInterface.getName(), renderInterface(anInterface)));
    }

    private void saveInterface(final String interfaceName, final String javaInterface) {
        if (!Paths.get(packageDirectory).toFile().exists()) {
            try {
                Files.createDirectories(Paths.get(packageDirectory));
            } catch (IOException e) {
                System.err.println("Could not create target directory: " + packageDirectory);
                e.printStackTrace();
            }
        }
        final String filePath = packageDirectory + File.separator + interfaceName + DEFAULT_FILE_ENDING;
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
            out.write(javaInterface);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Could not write to file!");
            e.printStackTrace();
        }
    }
}
