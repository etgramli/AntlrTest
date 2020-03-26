package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.graph.GraphBuilder;
import de.etgramlich.dsl.graph.type.BnfRuleGraph;
import de.etgramlich.dsl.graph.type.Node;
import de.etgramlich.dsl.graph.type.NodeEdge;
import de.etgramlich.dsl.graph.type.NodeType;
import de.etgramlich.dsl.parser.type.Alternatives;
import de.etgramlich.dsl.parser.type.BnfRule;
import de.etgramlich.dsl.parser.type.Sequence;
import de.etgramlich.dsl.parser.type.repetition.Precedence;
import de.etgramlich.dsl.parser.type.repetition.ZeroOrMore;
import de.etgramlich.dsl.parser.type.text.Keyword;
import de.etgramlich.dsl.parser.type.text.NonTerminal;
import de.etgramlich.dsl.parser.type.text.Type;
import de.etgramlich.dsl.util.StringUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class InterfaceBuilderTest {
    private static final String DUMMY_DIRECTORY = ".";
    private static final String DUMMY_PACKAGE = "com.dummy";

    @Test
    void renderInterface_interfaceWithOnlyName() {
        final Interface anInterface = new Interface("Test", Collections.emptySet(), Collections.emptySet());
        final String expected = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                        " interface Test { }");

        try {
            final InterfaceBuilder builder = new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
            assertEquals(expected, StringUtil.removeAllWhiteSpaces(builder.renderInterface(anInterface)));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void renderInterface_interfaceWithOneParent() {
        final Set<String> parents = Set.of("SuperInterface");
        final Interface anInterface = new Interface("Test", parents, Collections.emptySet());
        final String expected = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                        " interface Test extends " + parents.iterator().next() + "{ }");

        try {
            final InterfaceBuilder builder = new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
            assertEquals(expected, StringUtil.removeAllWhiteSpaces(builder.renderInterface(anInterface)));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void renderInterface_interfaceWithMultipleParents() {
        final Set<String> parents = Set.of("SuperInterface", "OtherParent");
        final Iterator<String> parentIterator = parents.iterator();
        final Interface anInterface = new Interface("Test", parents, Collections.emptySet());
        final String expected = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                "interface Test extends " + parentIterator.next() + ", " + parentIterator.next() + "{ }");

        try {
            final InterfaceBuilder builder = new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
            assertEquals(expected, StringUtil.removeAllWhiteSpaces(builder.renderInterface(anInterface)));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void renderInterface_interfaceNoParentsTwoMethodsNoArguments() {
        final Set<Method> methods = Set.of(
                new Method("int", "getX"),
                new Method("int", "getY")
        );

        final Interface anInterface = new Interface("Test", Collections.emptySet(), methods);
        final String expectedA = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                " interface Test {" +
                    "int getX();" +
                    "int getY();" +
                " }");
        final String expectedB = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                " interface Test {" +
                    "int getY();" +
                    "int getX();" +
                " }");

        try {
            final InterfaceBuilder builder = new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
            final String interfaceString = StringUtil.removeAllWhiteSpaces(builder.renderInterface(anInterface));
            assertTrue(interfaceString.equals(expectedA) || interfaceString.equals(expectedB));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void renderInterface_interfaceNoParentsTwoMethodsTwoArgumentsEach() {
        final Set<Method> methods = Set.of(
                new Method("int", "getX", List.of(
                        new Argument("int", "a"),
                        new Argument("int", "b")
                )),
                new Method("int", "getY", List.of(
                        new Argument("int", "a"),
                        new Argument("int", "b")
                ))
        );

        final Interface anInterface = new Interface("Test", Collections.emptySet(), methods);
        final String expectedA = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                " interface Test {" +
                    "int getX(int a, int b);" +
                    "int getY(int a, int b);" +
                " }");
        final String expectedB = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                "interface Test {" +
                    "int getY(int a, int b);" +
                    "int getX(int a, int b);" +
                " }");

        try {
            final InterfaceBuilder builder = new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
            final String interfaceString = StringUtil.removeAllWhiteSpaces(builder.renderInterface(anInterface));
            assertTrue(interfaceString.equals(expectedA) || interfaceString.equals(expectedB));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void renderInterface_joi() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("joi"),
                        new Alternatives(List.of(
                                new Sequence(List.of(
                                        new Precedence(new Alternatives(List.of(
                                                new Sequence(List.of(new Keyword("'component'"))),
                                                new Sequence(List.of(new Keyword("'singleton'")))))),
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
        try {
            final InterfaceBuilder ib = new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
            final Set<Interface> interfaces = ib.getInterfaces(graph);
            assertEquals(10, interfaces.size());

            final Set<String> keywords = graph.edgeSet().stream()
                    .filter(edge -> edge instanceof NodeEdge)
                    .map(edge -> ((NodeEdge) edge).getNode())
                    .filter(node -> node.getType().equals(NodeType.KEYWORD))
                    .map(Node::getName)
                    .collect(Collectors.toUnmodifiableSet());
            final Set<String> methodNames = interfaces.stream()
                    .flatMap(anInterface -> anInterface.getMethods().stream())
                    .map(Method::getName)
                    .collect(Collectors.toUnmodifiableSet());
            assertEquals(keywords, methodNames);

            final Set<String> types = graph.edgeSet().stream()
                    .filter(edge -> edge instanceof NodeEdge)
                    .map(edge -> ((NodeEdge) edge).getNode())
                    .filter(node -> node.getType().equals(NodeType.TYPE))
                    .map(Node::getName)
                    .collect(Collectors.toUnmodifiableSet());
            final Set<String> arguments = interfaces.stream()
                    .flatMap(anInterface -> anInterface.getMethods().stream())
                    .flatMap(method -> method.getArguments().stream())
                    .map(Argument::getType)
                    .collect(Collectors.toUnmodifiableSet());
            assertEquals(types, arguments);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getInterfaces() {
    }

    @Test
    void getInterfacesToSave_junit() {
        final BnfRuleGraph graph = new GraphBuilder(new BnfRule(new NonTerminal("joi"),
                new Alternatives(List.of(
                        new Sequence(List.of(
                                new Precedence(new Alternatives(List.of(
                                        new Sequence(List.of(new Keyword("'component'"))),
                                        new Sequence(List.of(new Keyword("'singleton'")))))),
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
        final Set<String> expectedInterfaces = Set.of("Scope_0", "Scope_4", "Scope_6", "Scope_9", "Scope_8", "Scope_11", "Scope_14", "Scope_13", "Scope_17", "Scope_16");
        final InterfaceBuilder builder;
        try {
            builder = new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
            assertEquals(expectedInterfaces, builder.getInterfacesToSave(graph));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
