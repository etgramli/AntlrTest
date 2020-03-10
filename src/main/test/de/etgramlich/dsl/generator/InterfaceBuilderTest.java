package de.etgramlich.dsl.generator;

import de.etgramlich.dsl.util.StringUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceBuilderTest {
    private static final String DUMMY_DIRECTORY = ".";
    private static final String DUMMY_PACKAGE = "com.dummy";

    @Test
    void renderInterface_interfaceWithOnlyName() {
        final Interface anInterface = new Interface("Test", Collections.emptySet(), Collections.emptySet());
        final String expected = "package " + DUMMY_PACKAGE + ";\n interface Test { }";

        try {
            Method method = InterfaceBuilder.class.getDeclaredMethod("renderInterface", Interface.class);
            method.setAccessible(true);
            final String interfaceString = (String) method.invoke(new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE), anInterface);
            assertEquals(StringUtil.removeAllWhiteSpaces(expected), StringUtil.removeAllWhiteSpaces(interfaceString));
        } catch (NoSuchMethodException | IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void renderInterface_interfaceWithOneParent() {
        final Set<String> parents = Set.of("SuperInterface");
        final Interface anInterface = new Interface("Test", parents, Collections.emptySet());
        final String expected = "package " + DUMMY_PACKAGE + ";\n interface Test extends " + parents.iterator().next() + "{ }";

        try {
            Method method = InterfaceBuilder.class.getDeclaredMethod("renderInterface", Interface.class);
            method.setAccessible(true);
            final String interfaceString = (String) method.invoke(new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE), anInterface);
            assertEquals(StringUtil.removeAllWhiteSpaces(expected), StringUtil.removeAllWhiteSpaces(interfaceString));
        } catch (NoSuchMethodException | IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void renderInterface_interfaceWithMultipleParents() {
        final Set<String> parents = Set.of("SuperInterface", "OtherParent");
        final Iterator<String> parentIterator = parents.iterator();
        final Interface anInterface = new Interface("Test", parents, Collections.emptySet());
        final String expected = "package " + DUMMY_PACKAGE + ";\n interface Test extends " + parentIterator.next() + ", " + parentIterator.next() + "{ }";

        try {
            Method method = InterfaceBuilder.class.getDeclaredMethod("renderInterface", Interface.class);
            method.setAccessible(true);
            final String interfaceString = (String) method.invoke(new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE), anInterface);
            assertEquals(StringUtil.removeAllWhiteSpaces(expected), StringUtil.removeAllWhiteSpaces(interfaceString));
        } catch (NoSuchMethodException | IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void renderInterface_interfaceNoParentsTwoMethodsNoArguments() {
        final Set<de.etgramlich.dsl.generator.Method> methods = Set.of(
                new de.etgramlich.dsl.generator.Method("int", "getX"),
                new de.etgramlich.dsl.generator.Method("int", "getY")
        );

        final Interface anInterface = new Interface("Test", Collections.emptySet(), methods);
        final String expectedA = StringUtil.removeAllWhiteSpaces("package " + DUMMY_PACKAGE + ";\n interface Test {" +
                "int getX();" +
                "int getY();" +
                " }");
        final String expectedB = StringUtil.removeAllWhiteSpaces("package " + DUMMY_PACKAGE + ";\n interface Test {" +
                "int getY();" +
                "int getX();" +
                " }");

        try {
            Method method = InterfaceBuilder.class.getDeclaredMethod("renderInterface", Interface.class);
            method.setAccessible(true);
            final String interfaceString = StringUtil.removeAllWhiteSpaces((String) method.invoke(new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE), anInterface));
            assertTrue(interfaceString.equals(expectedA) || interfaceString.equals(expectedB));
        } catch (NoSuchMethodException | IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void renderInterface_interfaceNoParentsTwoMethodsTwoArgumentsEach() {
        final Set<de.etgramlich.dsl.generator.Method> methods = Set.of(
                new de.etgramlich.dsl.generator.Method("int", "getX", List.of(
                        new Argument("int", "a"),
                        new Argument("int", "b")
                )),
                new de.etgramlich.dsl.generator.Method("int", "getY", List.of(
                        new Argument("int", "a"),
                        new Argument("int", "b")
                ))
        );

        final Interface anInterface = new Interface("Test", Collections.emptySet(), methods);
        final String expectedA = StringUtil.removeAllWhiteSpaces("package " + DUMMY_PACKAGE + ";\n interface Test {" +
                "int getX(int a, int b);" +
                "int getY(int a, int b);" +
                " }");
        final String expectedB = StringUtil.removeAllWhiteSpaces("package " + DUMMY_PACKAGE + ";\n interface Test {" +
                "int getY(int a, int b);" +
                "int getX(int a, int b);" +
                " }");

        try {
            Method method = InterfaceBuilder.class.getDeclaredMethod("renderInterface", Interface.class);
            method.setAccessible(true);
            final String interfaceString = StringUtil.removeAllWhiteSpaces((String) method.invoke(new InterfaceBuilder(DUMMY_DIRECTORY, DUMMY_PACKAGE), anInterface));
            assertTrue(interfaceString.equals(expectedA) || interfaceString.equals(expectedB));
        } catch (NoSuchMethodException | IOException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }
}
