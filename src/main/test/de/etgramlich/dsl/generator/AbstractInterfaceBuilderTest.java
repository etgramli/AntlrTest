package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.graph.GraphBuilder;
import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import de.etgramlich.dsl.graph.type.NodeType;
import de.etgramlich.dsl.graph.type.OptionalEdge;
import de.etgramlich.dsl.graph.type.Scope;
import de.etgramlich.dsl.graph.type.ScopeEdge;
import de.etgramlich.dsl.parser.type.Alternatives;
import de.etgramlich.dsl.parser.type.BnfRule;
import de.etgramlich.dsl.parser.type.Sequence;
import de.etgramlich.dsl.parser.type.repetition.Precedence;
import de.etgramlich.dsl.parser.type.repetition.ZeroOrMore;
import de.etgramlich.dsl.parser.type.text.Keyword;
import de.etgramlich.dsl.parser.type.text.NonTerminal;
import de.etgramlich.dsl.parser.type.text.Type;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AbstractInterfaceBuilderTest {
    private static final String DUMMY_DIRECTORY = ".";
    private static final String DUMMY_PACKAGE = "com.dummy";

    /**
     * Generate a set of strings containing the names of the interface to be saved of the passed graph.
     * @param graph BnfRuleGraph, must not be null.
     * @return Set of String, not null, may be empty.
     */
    private static Set<String> getInterfacesToSave(final BnfRuleGraph graph) {
        final Set<String> interfaces = new HashSet<>(Set.of(graph.getReadableString(graph.getStartScope())));

        final Set<Scope> toVisitNext = Collections.synchronizedSet(new HashSet<>());
        Scope currentScope = graph.getStartScope();

        while (currentScope != null) {
            Set<Scope> successorKeyword = graph.outGoingNodeEdges(currentScope).stream()
                    .filter(edge -> edge.getNode().getType().equals(NodeType.KEYWORD))
                    .map(ScopeEdge::getTarget)
                    .collect(Collectors.toUnmodifiableSet());
            for (Scope successor : successorKeyword) {
                final Set<Scope> successorType = graph.outGoingNodeEdges(successor).stream()
                        .filter(edge -> edge.getNode().getType().equals(NodeType.TYPE))
                        .map(ScopeEdge::getTarget)
                        .collect(Collectors.toUnmodifiableSet());

                if (successorType.isEmpty() && !interfaces.contains(successor.getName())) {
                    toVisitNext.add(successor);
                } else {
                    successorType.stream()
                            .filter(scope -> !interfaces.contains(graph.getReadableString(scope)))
                            .forEach(toVisitNext::add);
                }
            }
            graph.outgoingEdgesOf(currentScope).stream()
                    .filter(edge -> edge instanceof OptionalEdge)
                    .map(ScopeEdge::getTarget)
                    .filter(scope -> !interfaces.contains(graph.getReadableString(scope)))
                    .forEach(toVisitNext::add);

            if (!toVisitNext.isEmpty()) {
                currentScope = toVisitNext.iterator().next();
                toVisitNext.remove(currentScope);
                interfaces.add(graph.getReadableString(currentScope));
            } else {
                currentScope = null;
            }
        }
        return Collections.unmodifiableSet(interfaces);
    }

    @Test
    void getInterfaces_alternatives() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("joi"),
                new Alternatives(List.of(
                        new Sequence(List.of(new Keyword("component"))),
                        new Sequence(List.of(new Keyword("singleton"))))))).getGraph();

        final Set<Interface> interfaces = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE, null).getInterfaces(graph);
        assertEquals(2, interfaces.size());

        final String secondScopeName = "EndScope";
        final Set<Interface> expected = Set.of(
                new Interface("BeginScope", Collections.emptySet(), Set.of(
                        new Method(secondScopeName, "component"),
                        new Method(secondScopeName, "singleton"))),
                new Interface(secondScopeName, Collections.emptySet(), Set.of(
                        new Method("void", "end")
                )));
        assertEquals(expected, interfaces);
    }

    @Test
    void getInterfaces_sequence() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("joi"),
                new Alternatives(List.of(
                        new Sequence(List.of(
                                new Keyword("component"),
                                new Keyword("singleton"),
                                new Keyword("iface"))))))).getGraph();

        final Set<Interface> interfaces = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE, null).getInterfaces(graph);
        final Set<Interface> expected = Set.of(
                new Interface("BeginScope", Collections.emptySet(), Set.of(
                        new Method("SingletonScope", "component"))),
                new Interface("SingletonScope", Collections.emptySet(), Set.of(
                        new Method("IfaceScope", "singleton"))),
                new Interface("IfaceScope", Collections.emptySet(), Set.of(
                        new Method("EndScope", "iface"))),
                new Interface("EndScope", Collections.emptySet(), Set.of(
                        new Method("void", "end")
                )));
        assertEquals(expected, interfaces);
    }

    @Test
    void getInterfaces_alternative() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("joi"),
                new Alternatives(List.of(
                        new Sequence(List.of(new Keyword("component"))),
                        new Sequence(List.of(new Keyword("singleton"))),
                        new Sequence(List.of(new Keyword("iface"))))))).getGraph();
        final Set<String> expected = Set.of("BeginScope", "EndScope");

        assertEquals(expected, getInterfacesToSave(graph));

        final Set<String> actual = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE, null).getInterfaces(graph).stream()
                .map(Interface::getName)
                .collect(Collectors.toUnmodifiableSet());
        assertEquals(expected, actual);
    }

    @Test
    void getInterfaces_loop() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("joi"),
                new Alternatives(List.of(
                        new Sequence(List.of(new ZeroOrMore(new Alternatives(List.of(
                                new Sequence(List.of(new Keyword("component"))),
                                new Sequence(List.of(new Keyword("singleton"))),
                                new Sequence(List.of(new Keyword("iface")))))))))))).getGraph();

        final Set<Interface> interfaces = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE, null).getInterfaces(graph);
        assertEquals(3, interfaces.size());

        final Set<Interface> expected = Set.of(
                new Interface("BeginScope", Set.of("ComponentIfaceSingletonScope"), Collections.emptySet()),
                new Interface("ComponentIfaceSingletonScope", Set.of("EndScope"), Set.of(
                        new Method("ComponentIfaceSingletonScope", "component"),
                        new Method("ComponentIfaceSingletonScope", "singleton"),
                        new Method("ComponentIfaceSingletonScope", "iface"))),
                new Interface("EndScope", Collections.emptySet(), Set.of(
                        new Method("void", "end")
                )));
        assertEquals(expected, interfaces);
    }

    @Test
    void getInterfacesToSave_sequence() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("sequence"),
                new Alternatives(List.of(
                        new Sequence(List.of(new Keyword("K_0"), new Keyword("K_1"), new Keyword("K_2")))
                )))).getGraph();
        final Set<String> expected = Set.of("BeginScope", "K_1Scope", "K_2Scope", "EndScope");

        assertEquals(expected, getInterfacesToSave(graph));

        final Set<String> actual = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE, null).getInterfaces(graph).stream()
                .map(Interface::getName)
                .collect(Collectors.toUnmodifiableSet());
        assertEquals(expected, actual);
    }

    @Test
    void getInterfacesToSave_joi() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("joi"),
                new Alternatives(List.of(
                        new Sequence(List.of(
                                new Precedence(new Alternatives(List.of(
                                        new Sequence(List.of(new Keyword("component"))),
                                        new Sequence(List.of(new Keyword("singleton")))))),
                                new Type("String"),
                                new Keyword("impl"), new Type("String"),
                                new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(
                                        new Keyword("impl"), new Type("String")))))),
                                new Keyword("method"), new Type("String"),
                                new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(
                                        new Keyword("method"), new Type("String")))))),
                                new ZeroOrMore(new Alternatives(List.of(new Sequence(List.of(
                                        new Keyword("field"), new Type("String"))))))
                        )))))).getGraph();

        final Set<Interface> interfaces = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE, null).getInterfaces(graph);

        final Interface begin = interfaces.stream().filter(i -> i.getName().equals("BeginScope")).findAny().get();
        Set<Method> methods = Set.of(
                new Method("ImplScopeScope4", "component", new Argument("String", "string")),
                new Method("ImplScopeScope4", "singleton", new Argument("String", "string"))
        );
        assertEquals(methods, begin.getMethods());
        assertEquals(Collections.emptySet(), begin.getParents());

        final Interface methodInterface = interfaces.stream().filter(i -> i.getName().equals("MethodScope")).findAny().get();
        methods = Set.of(new Method("MethodScope", "method", new Argument("String", "string")));
        assertEquals(methods, methodInterface.getMethods());
        assertEquals(1, methodInterface.getParents().size());

        final Interface fieldInterface = interfaces.stream().filter(i -> i.getName().equals("FieldScope")).findAny().get();
        methods = Set.of(new Method("FieldScope", "field", new Argument("String", "string")));
        assertEquals(methods, fieldInterface.getMethods());
        assertEquals(Set.of("EndScope"), fieldInterface.getParents());
    }
}