package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import de.etgramlich.dsl.graph.type.NodeEdge;
import de.etgramlich.dsl.graph.type.NodeType;
import de.etgramlich.dsl.graph.type.OptionalEdge;
import de.etgramlich.dsl.graph.type.Scope;
import de.etgramlich.dsl.graph.type.ScopeEdge;
import de.etgramlich.dsl.util.StringUtil;
import de.etgramlich.dsl.util.SymbolTable;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractInterfaceBuilder implements InterfaceBuilder {
    /**
     * File name of the scala interface template.
     */
    private static final String TEMPLATE_FILENAME = "interfaceTemplates.stg";

    /**
     * Loaded template file to be reused.
     */
    protected static final STGroup ST_GROUP;

    static {
        final URL templateFileUrl = Thread.currentThread().getContextClassLoader().getResource(TEMPLATE_FILENAME);
        if (templateFileUrl == null) {
            throw new RuntimeException("Template fie interfaceTemplates.stg could not be read!");
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
     * File ending for the interfaces.
     */
    private final String fileEnding;

    /**
     * Return type of the end method.
     */
    private final String returnType;

    /**
     * Saves already saved interfaces, contains mapping from grammar keywords to java keywords.
     */
    private final SymbolTable symbolTable;

    protected AbstractInterfaceBuilder(final String targetDirectory,
                                       final String targetPackage,
                                       final String fileEnding,
                                       final String returnType) {
        if (StringUtils.isBlank(targetDirectory)) {
            throw new IllegalArgumentException("Target directory must not be blank!");
        }
        if (StringUtils.isBlank(targetPackage)) {
            throw new IllegalArgumentException("Target package must not be blank!");
        }
        if (StringUtil.isBlank(fileEnding)) {
            throw new IllegalArgumentException("File ending must not be blank!");
        }
        if (returnType != null && StringUtil.isBlank(returnType)) {
            throw new IllegalArgumentException("Return type must be null or not be blank! (was: " + returnType + ")");
        }
        this.targetPackage = targetPackage;
        this.packageDirectory = targetDirectory + File.separator + targetPackage.replace('.', File.separatorChar);
        this.fileEnding = fileEnding;
        this.returnType = returnType;
        this.symbolTable = new SymbolTable();
    }

    /**
     * Returns the target package for the interface files.
     * @return String, not blank.
     */
    public String getTargetPackage() {
        return targetPackage;
    }

    /**
     * Returns the directory of the directory representing the package.
     * @return String, not blank.
     */
    public String getPackageDirectory() {
        return packageDirectory;
    }

    abstract String renderInterface(Interface anInterface);

    @Override
    public final Set<Interface> getInterfaces(final BnfRuleGraph graph) {
        if (!graph.isConsistent()) {
            throw new IllegalArgumentException("Graph is not consistent!");
        }

        final Deque<Scope> toVisitNext = new ArrayDeque<>(graph.vertexSet().size());
        final Set<Interface> interfaces = new HashSet<>(graph.vertexSet().size());

        // Used to detect collision of readable scope names
        final Map<String, String> scopeToReadable = new HashMap<>(graph.vertexSet().size());

        for (Scope currentScope = graph.getEndScope(); currentScope != null; currentScope = toVisitNext.pollFirst()) {
            final Interface currentInterface = getInterface(currentScope, graph);
            if (scopeToReadable.containsValue(currentInterface.getName())) {
                currentInterface.setName(currentInterface.getName() + currentScope.getName());
            }
            interfaces.add(currentInterface);
            scopeToReadable.put(currentScope.getName(), currentInterface.getName());
            symbolTable.addType(currentInterface.getName());

            graph.getPrecedingKeywords(currentScope).stream()
                    .filter(Predicate.not(scope -> scopeToReadable.containsKey(scope.getName())))
                    .filter(Predicate.not(toVisitNext::contains))
                    .forEach(toVisitNext::add);
            graph.incomingEdgesOf(currentScope).stream()
                    .filter(edge -> edge instanceof OptionalEdge)
                    .map(ScopeEdge::getSource)
                    .filter(Predicate.not(scope -> scopeToReadable.containsKey(scope.getName())))
                    .filter(Predicate.not(toVisitNext::contains))
                    .forEach(toVisitNext::add);
        }

        return interfaces.stream()
                .map(anInterface -> replaceTypeNames(anInterface, scopeToReadable))
                .collect(Collectors.toUnmodifiableSet());
    }

    private Interface replaceTypeNames(final Interface anInterface, final Map<String, String> scopeToReadable) {
        final Set<String> newParents = anInterface.getParents().stream()
                .map(scopeToReadable::get)
                .collect(Collectors.toUnmodifiableSet());
        final Set<Method> methods = anInterface.getMethods().stream()
                .map(m -> scopeToReadable.containsKey(m.getReturnType())
                        ? new Method(scopeToReadable.get(m.getReturnType()), m.getName(), m.getArguments())
                        : m)
                .collect(Collectors.toUnmodifiableSet());

        return new Interface(anInterface.getName(), newParents, methods);
    }

    protected final Interface getInterface(final Scope currentScope, final BnfRuleGraph graph) {
        final Set<String> parents = getParents(currentScope, graph).stream()
                .map(Scope::getName)
                .collect(Collectors.toUnmodifiableSet());
        final Set<Method> methods = currentScope == graph.getEndScope()
                ? Set.of(new Method(returnType == null ? "void" : returnType, "end"))
                : getMethods(currentScope, graph);

        return new Interface(graph.getReadableString(currentScope), parents, methods);
    }

    protected static Set<Scope> getParents(final Scope currentScope, final BnfRuleGraph graph) {
        return graph.outGoingOptionalEdges(currentScope).stream()
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
            final String returnType = graph.getScopeAfterTypeEdges(edge.getTarget()).getName();
            return subsequent.stream()
                    .map(scope -> new Method(returnType, edge.getNode().getName(), getArguments(edge, graph)))
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    /**
     * Generates arguments for a method represented by the given edge in the graph.
     * @param nodeEdge Edge representing the method (must be of type Keyword), must be present in the graph.
     * @param graph Valid BnfRuleGraph, must not be null.
     * @return List of Arguments, may be empty.
     */
    private List<Argument> getArguments(final NodeEdge nodeEdge, final BnfRuleGraph graph) {
        if (nodeEdge == null) {
            throw new IllegalArgumentException("NodeEdge must not be null!");
        }
        if (!graph.containsEdge(nodeEdge)) {
            throw new IllegalArgumentException("NodeEdge must be present in the graph!");
        }
        if (!nodeEdge.getNode().getType().equals(NodeType.KEYWORD)) {
            throw new IllegalArgumentException("NodeEdge must be of type KEYWORD!");
        }

        final List<Argument> arguments = new ArrayList<>();
        Set<NodeEdge> outgoingTypeEdges = graph.outGoingNodeEdges(nodeEdge.getTarget(), NodeType.TYPE);
        if (outgoingTypeEdges.size() > 1) {
            throw new IllegalArgumentException("The KEYWORD edge must be followed by exactly one edge!");
        }

        final Map<String, Integer> nameIndices = new HashMap<>();
        while (!outgoingTypeEdges.isEmpty()) {
            if (outgoingTypeEdges.size() > 1) {
                throw new IllegalArgumentException("TYPE edge must be followed by exactly one TYPE edge!");
            }

            final String nextType = outgoingTypeEdges.iterator().next().getNode().getName();
            final String nextName = getArgumentName(nextType);
            nameIndices.put(nextName,  nameIndices.getOrDefault(nextName, 0) + 1);

            arguments.add(new Argument(nextType, nextName));

            outgoingTypeEdges = graph.outGoingNodeEdges(outgoingTypeEdges.iterator().next().getTarget(), NodeType.TYPE);
        }
        if (nameIndices.values().stream().anyMatch(integer -> integer > 1)) {
            return indexArgumentNames(arguments);
        } else {
            return arguments;
        }
    }

    private static List<Argument> indexArgumentNames(final List<Argument> arguments) {
        final List<Argument> indexed = new ArrayList<>(arguments.size());
        final Map<String, Integer> nameIndices = new HashMap<>();

        for (Argument argument : arguments) {
            final String name = argument.getName();
            final int index = nameIndices.getOrDefault(name, 0);
            nameIndices.put(name, index + 1);
            indexed.add(new Argument(argument.getType(), name + index));
        }
        return indexed;
    }

    private String getArgumentName(final String typeName) {
        final String suggested = StringUtil.firstCharToLowerCase(typeName);
        if (symbolTable.isValidName(suggested)) {
            return suggested;
        }
        final String suggestedUpperCase = StringUtil.firstCharToUpperCase(suggested);
        switch (suggestedUpperCase.charAt(0)) {
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
                return "an" + suggestedUpperCase;
            default:
                return "a" + suggestedUpperCase;
        }
    }

    @Override
    public final void saveInterfaces(final Set<Interface> interfaces) {
        interfaces.forEach(anInterface -> saveInterface(anInterface.getName(), renderInterface(anInterface)));
    }

    private void saveInterface(final String interfaceName, final String javaInterface) {
        if (!Paths.get(getPackageDirectory()).toFile().exists()) {
            try {
                Files.createDirectories(Paths.get(getPackageDirectory()));
            } catch (IOException e) {
                System.err.println("Could not create target directory: " + getPackageDirectory());
                e.printStackTrace();
            }
        }
        final String filePath = getPackageDirectory() + File.separator + interfaceName + fileEnding;
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
