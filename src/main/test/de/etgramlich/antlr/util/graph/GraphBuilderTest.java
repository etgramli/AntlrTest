package de.etgramlich.antlr.util.graph;

import de.etgramlich.antlr.parser.type.rhstype.Alternative;
import de.etgramlich.antlr.parser.type.rhstype.Element;
import de.etgramlich.antlr.parser.type.terminal.Id;
import de.etgramlich.antlr.parser.type.terminal.Text;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
class GraphBuilderTest {
    // sequenz: (cinoibebt, name)
    private static final String JOI_GRAMMAR = "<component> ::= 'component' <name> 'impl' <componentInterface> {'impl' <componentInterface>} (<componentMethod>) {<componentField>}";
    private static final Text TEXT_FIRST = new Text("Text First");
    private static final Text TEXT_SECOND = new Text("Text Second");
    private static final Id ID_FIRST = new Id("ID First");
    private static final Id ID_SECOND = new Id("ID Second");

    @Test
    void addRuleToGraph() {
    }

    @Test
    void test_getRuleNodes_joi() {
        // ToDo
    }

    @Test
    void test_getAlternativeScopes_oneTerminalOneNonTerminal_returnsOneListInList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<Alternative> alternatives = List.of(
                new Alternative(List.of(new Element(TEXT_FIRST))),
                new Alternative(List.of(new Element(ID_FIRST))));

        Method method = GraphBuilder.class.getDeclaredMethod("getAlternativeScopes", List.class);
        method.setAccessible(true);
        final List<List<Alternative>> resultList = (List<List<Alternative>>) method.invoke(null, alternatives);

        assertEquals(1, resultList.size());
        assertEquals(2, resultList.get(0).size());
    }


    @Test
    void test_getAlternativeScopes_twoTerminalsFiveNonTerminal_returnsTwoListsInList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<Alternative> alternatives = List.of(
                new Alternative(List.of(new Element(TEXT_FIRST))),
                new Alternative(List.of(new Element(ID_FIRST))),
                new Alternative(List.of(new Element(ID_SECOND))),
                new Alternative(List.of(new Element(TEXT_SECOND))),
                new Alternative(List.of(new Element(ID_SECOND))));

        Method method = GraphBuilder.class.getDeclaredMethod("getAlternativeScopes", List.class);
        method.setAccessible(true);
        final List<List<Alternative>> resultList = (List<List<Alternative>>) method.invoke(null, alternatives);

        assertEquals(2, resultList.size());
        assertEquals(3, resultList.get(0).size());
        assertEquals(2, resultList.get(1).size());
    }
}