package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.util.StringUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
                "package " + DUMMY_PACKAGE + ";" +StringUtil.NEWLINE +
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
            final String interfaceString = StringUtil.removeAllWhiteSpaces(new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE).renderInterface(anInterface));
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
}
