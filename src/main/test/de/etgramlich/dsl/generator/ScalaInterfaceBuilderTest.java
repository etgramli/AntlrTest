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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ScalaInterfaceBuilderTest {

    private static final String DUMMY_DIRECTORY = ".";
    private static final String DUMMY_PACKAGE = "com.dummy";

    @Test
    void renderInterface_interfaceWithOnlyName() {
        final Interface anInterface = new Interface("Test", Collections.emptySet(), Collections.emptySet());
        final String expected = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                        " trait Test { }");
        final ScalaInterfaceBuilder builder = new ScalaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
        assertEquals(expected, StringUtil.removeAllWhiteSpaces(builder.renderInterface(anInterface)));
    }

    @Test
    void renderInterface_interfaceWithOneParent() {
        final Set<String> parents = Set.of("SuperInterface");
        final Interface anInterface = new Interface("Test", parents, Collections.emptySet());
        final String expected = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                        " trait Test extends " + parents.iterator().next() + "{ }");
        final ScalaInterfaceBuilder builder = new ScalaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
        assertEquals(expected, StringUtil.removeAllWhiteSpaces(builder.renderInterface(anInterface)));
    }

    @Test
    void renderInterface_interfaceWithMultipleParents() {
        final Set<String> parents = Set.of("SuperInterface", "OtherParent");
        final Iterator<String> parentIterator = parents.iterator();
        final Interface anInterface = new Interface("Test", parents, Collections.emptySet());
        final String expected = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                        "trait Test extends " + parentIterator.next() + " with " + parentIterator.next() + "{ }");
        final ScalaInterfaceBuilder builder = new ScalaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
        assertEquals(expected, StringUtil.removeAllWhiteSpaces(builder.renderInterface(anInterface)));
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
                        " trait Test {" +
                        "def getX() : int" +
                        "def getY() : int" +
                        " }");
        final String expectedB = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                        " trait Test {" +
                        "def getY() : int" +
                        "def getX() : int" +
                        " }");
        final ScalaInterfaceBuilder builder = new ScalaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
        final String interfaceString = StringUtil.removeAllWhiteSpaces(builder.renderInterface(anInterface));
        assertTrue(interfaceString.equals(expectedA) || interfaceString.equals(expectedB));
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
                        " trait Test {" +
                        "def getX(a : int, b : int) : int" +
                        "def getY(a : int, b : int) : int" +
                        " }");
        final String expectedB = StringUtil.removeAllWhiteSpaces(
                "package " + DUMMY_PACKAGE + ";" + StringUtil.NEWLINE +
                        "trait Test {" +
                        "def getY(a : int, b : int) : int" +
                        "def getX(a : int, b : int) : int" +
                        " }");
        final ScalaInterfaceBuilder builder = new ScalaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
        final String interfaceString = StringUtil.removeAllWhiteSpaces(builder.renderInterface(anInterface));
        assertTrue(interfaceString.equals(expectedA) || interfaceString.equals(expectedB));
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

        final ScalaInterfaceBuilder ib = new ScalaInterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE);
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
    }
}