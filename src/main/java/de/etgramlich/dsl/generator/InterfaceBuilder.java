package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.util.CollectionUtil;
import de.etgramlich.dsl.util.StringUtil;
import de.etgramlich.dsl.util.SymbolTable;
import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import de.etgramlich.dsl.graph.type.NodeType;
import de.etgramlich.dsl.graph.type.NodeEdge;
import de.etgramlich.dsl.graph.type.Scope;
import de.etgramlich.dsl.graph.type.Node;
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
import java.util.Set;
import java.util.HashSet;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;

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
     * Graph to build interfaces from. Private methods need reference.
     */
    private BnfRuleGraph graph;

    /**
     * Saves already saved interfaces, contains mapping from grammar keywords to java keywords.
     */
    private final SymbolTable symbolTable;

    /**
     * Creates InterfaceBuilder with target (root) directory and package of the interfaces.
     *
     * @param targetDirectory Target directory to contain all interfaces.
     * @param targetPackage   Package of the interfaces (a subfolder will be created in target directory).
     * @throws IOException Throws exception if the target directory can not be created.
     */
    public InterfaceBuilder(final String targetDirectory, final String targetPackage) throws IOException {
        if (StringUtils.isBlank(targetPackage)) {
            throw new IllegalArgumentException("Target package must not be blank!");
        }
        if (StringUtils.isBlank(targetDirectory)) {
            throw new IllegalArgumentException("Target directory must not be blank!");
        }
        this.targetPackage = targetPackage;
        packageDirectory = targetDirectory + File.separator + targetPackage.replace('.', File.separatorChar);
        Files.createDirectories(Paths.get(packageDirectory));
        symbolTable = new SymbolTable();
    }

    /**
     * Save all interfaces for the scopes in the graph.
     *
     * @param graph BnfRuleGraph, must not be null and consistent.
     */
    public void saveInterfaces(final BnfRuleGraph graph) {
        if (!graph.isConsistent()) {
            throw new IllegalArgumentException("Graph is not consistent!");
        }
        this.graph = graph;

        final Deque<Scope> toVisitNext = new ArrayDeque<>(graph.vertexSet().size());
        final Set<Interface> interfaces = new HashSet<>(graph.vertexSet().size());

        Scope currentScope = graph.getEndScope();
        while (currentScope != null) {
            final Interface currentInterface = getInterface(currentScope);
            if (!symbolTable.allCustomType(currentInterface.getParents())) {
                throw new NullPointerException("Not all parent interfaces found!");
            }
            saveInterface(currentInterface.getName(), renderInterface(currentInterface));
            interfaces.add(currentInterface);
            symbolTable.addType(currentInterface.getName());

            toVisitNext.addAll(graph.getPrecedingKeywords(currentScope).stream()
                    .filter(scope -> !symbolTable.isType(scope.getName()))
                    .collect(Collectors.toUnmodifiableSet()));
            currentScope = toVisitNext.pollFirst();
        }

        final Set<String> unsavedTypes = typesToBeSaved(interfaces).stream()
                .filter(type -> !symbolTable.isType(type))
                .collect(Collectors.toUnmodifiableSet());
        if (!unsavedTypes.isEmpty()) {
            throw new IllegalArgumentException("Not all scopes saved as interfaces: "
                    + CollectionUtil.asString(unsavedTypes));
        }
    }

    private Set<String> typesToBeSaved(final Set<Interface> interfaces) {
        final Set<String> types = new HashSet<>();
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

    private Interface getInterface(final Scope currentScope) {
        final Set<String> parents = graph.incomingEdgesOf(currentScope).stream()
                .filter(edge -> graph.isBackwardEdge(edge))
                .map(edge -> edge.getSource().getName())
                .collect(Collectors.toUnmodifiableSet());
        return new Interface(currentScope.getName(), parents, getMethods(currentScope));
    }

    /**
     * Converts a scope to Methods to be in the respective Interface.
     *
     * @param scope Scope to be converted to interface, to query its Methods.
     * @return List of Methods, not null, may be empty.
     */
    private Set<Method> getMethods(final Scope scope) {
        final Set<Method> methods = new HashSet<>(graph.outGoingNodeEdges(scope).size());
        for (NodeEdge nodeEdge : graph.outGoingNodeEdges(scope)) {
            if (nodeEdge.getNode().getType().equals(NodeType.KEYWORD)) {
                methods.add(new Method(nodeEdge.getTarget().getName(),
                        nodeEdge.getNode().getName(),
                        getArgument(nodeEdge)));
            } else {
                throw new IllegalArgumentException("Method requires a Keyword Node! was: "
                        + nodeEdge.getNode().getType().toString());
            }
        }
        return Collections.unmodifiableSet(methods);
    }

    private Argument getArgument(final NodeEdge nodeEdge) {
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

        return new Argument(follower.getName(), follower.getName().toLowerCase(Locale.ENGLISH));
    }

    /**
     * Saves a Java Interface with the given name and Methods.
     *
     * @param anInterface Interface to be saved, must not be null.
     * @return Interface representation as String.
     */
    protected String renderInterface(final Interface anInterface) {
        if (StringUtils.isBlank(anInterface.getName())) {
            throw new IllegalArgumentException("Interface name must not be blank!");
        }
        if (!StringUtil.startsWithUpperCase(anInterface.getName())) {
            throw new IllegalArgumentException("Interface name must start with an upper case letter!");
        }

        final ST st = ST_GROUP.getInstanceOf(INTERFACE_NAME);
        st.add("package", targetPackage);
        st.add("interfaceName", anInterface.getName());

        final List<String> parents = List.copyOf(anInterface.getParents());
        if (!parents.isEmpty()) {
            st.add("firstParent", parents.get(0));
            if (parents.size() > 1) {
                st.add("parents", parents.subList(1, parents.size()));
            }
        }

        st.add("methods", anInterface.getMethods());

        return st.render();
    }

    private void saveInterface(final String interfaceName, final String javaInterface) {
        final String fullPath = packageDirectory + File.separator + interfaceName + DEFAULT_FILE_ENDING;
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fullPath), StandardCharsets.UTF_8)) {
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
