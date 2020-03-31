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
        final Set<String> interfaces = new HashSet<>(Set.of(graph.getStartScope().getName()));

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
                successorType.stream()
                        .filter(scope -> !interfaces.contains(scope.getName()))
                        .forEach(toVisitNext::add);
                if (successorType.isEmpty() && !interfaces.contains(successor.getName())) {
                    toVisitNext.add(successor);
                }
            }
            graph.outgoingEdgesOf(currentScope).stream()
                    .filter(edge -> edge instanceof OptionalEdge)
                    .map(ScopeEdge::getTarget)
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

    @Test
    void getInterfaces_alternatives() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("joi"),
                new Alternatives(List.of(
                        new Sequence(List.of(new Keyword("component"))),
                        new Sequence(List.of(new Keyword("singleton"))))))).getGraph();

        final Set<Interface> interfaces = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE).getInterfaces(graph);
        assertEquals(2, interfaces.size());

        final String secondScopeName = "Scope_3";
        final Set<Interface> expected = Set.of(
                new Interface("Scope_0", Collections.emptySet(), Set.of(
                        new Method(secondScopeName, "component"),
                        new Method(secondScopeName, "singleton"))),
                new Interface(secondScopeName, Collections.emptySet(), Collections.emptySet()));
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

        final Set<Interface> interfaces = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE).getInterfaces(graph);
        final Set<Interface> expected = Set.of(
                new Interface("Scope_0", Collections.emptySet(), Set.of(
                        new Method("Scope_1", "component"))),
                new Interface("Scope_1", Collections.emptySet(), Set.of(
                        new Method("Scope_2", "singleton"))),
                new Interface("Scope_2", Collections.emptySet(), Set.of(
                        new Method("Scope_3", "iface"))),
                new Interface("Scope_3", Collections.emptySet(), Collections.emptySet()));
        assertEquals(expected, interfaces);
    }

    @Test
    void getInterfaces_alternative() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("joi"),
                new Alternatives(List.of(
                        new Sequence(List.of(new Keyword("component"))),
                        new Sequence(List.of(new Keyword("singleton"))),
                        new Sequence(List.of(new Keyword("iface"))))))).getGraph();
        final Set<String> expected = Set.of("Scope_0", "Scope_4");

        final JavaInterfaceBuilder builder = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
        assertEquals(expected, getInterfacesToSave(graph));

        final Set<String> interfaces = builder.getInterfaces(graph).stream()
                .map(Interface::getName)
                .collect(Collectors.toUnmodifiableSet());
        assertEquals(expected, interfaces);
    }

    @Test
    void getInterfaces_loop() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("joi"),
                new Alternatives(List.of(
                        new Sequence(List.of(new ZeroOrMore(new Alternatives(List.of(
                                new Sequence(List.of(new Keyword("component"))),
                                new Sequence(List.of(new Keyword("singleton"))),
                                new Sequence(List.of(new Keyword("iface")))))))))))).getGraph();

        final Set<Interface> interfaces = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE).getInterfaces(graph);
        assertEquals(3, interfaces.size());

        final Set<Interface> expected = Set.of(
                new Interface("Scope_0", Set.of("Scope_5"), Collections.emptySet()),
                new Interface("Scope_5", Set.of("Scope_4"), Set.of(
                        new Method("Scope_5", "component"),
                        new Method("Scope_5", "singleton"),
                        new Method("Scope_5", "iface"))),
                new Interface("Scope_4", Collections.emptySet(), Collections.emptySet()));
        assertEquals(expected, interfaces);
    }

    @Test
    void getInterfacesToSave_sequence() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("sequence"),
                new Alternatives(List.of(
                        new Sequence(List.of(new Keyword("K_0"), new Keyword("K_1"), new Keyword("K_2")))
                )))).getGraph();
        final Set<String> expected = Set.of("Scope_0", "Scope_1", "Scope_2", "Scope_3");

        final JavaInterfaceBuilder builder = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
        assertEquals(expected, getInterfacesToSave(graph));

        final Set<String> actual = builder.getInterfaces(graph).stream()
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
        final Set<String> expectedInterfaces =
                Set.of("Scope_0", "Scope_4", "Scope_6", "Scope_9", "Scope_8", "Scope_11", "Scope_14", "Scope_13",
                        "Scope_17", "Scope_16");

        final JavaInterfaceBuilder builder = new JavaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
        assertEquals(expectedInterfaces, getInterfacesToSave(graph));

        final Set<String> actual = builder.getInterfaces(graph).stream()
                .map(Interface::getName)
                .collect(Collectors.toUnmodifiableSet());
        assertEquals(expectedInterfaces, actual);
    }
}