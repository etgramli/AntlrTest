package de.etgramlich.util.graph;

import de.etgramlich.parser.type.Keyword;
import de.etgramlich.parser.type.NonTerminal;
import de.etgramlich.parser.type.Sequence;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
class GraphBuilderTest {
    // sequenz: (cinoibebt, name)
    private static final String JOI_GRAMMAR = "<component> ::= 'component' <name> 'impl' <componentInterface> {'impl' <componentInterface>} (<componentMethod>) {<componentField>}";
    private static final Keyword TEXT_FIRST  = new Keyword("Text First");
    private static final Keyword TEXT_SECOND = new Keyword("Text Second");
    private static final NonTerminal ID_FIRST = new NonTerminal("ID First");
    private static final NonTerminal ID_SECOND = new NonTerminal("ID Second");

    @Test
    void addRuleToGraph() {
    }

    @Test
    void test_getRuleNodes_joi() {
        // ToDo
    }

    @Test
    void test_getAlternativeScopes_oneTerminalOneNonTerminal_returnsOneListInList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<Sequence> alternatives = List.of(
                new Sequence(List.of(TEXT_FIRST)),
                new Sequence(List.of(ID_FIRST)));

        Method method = GraphBuilder.class.getDeclaredMethod("getAlternativeScopes", List.class);
        method.setAccessible(true);
        final List<List<Sequence>> resultList = (List<List<Sequence>>) method.invoke(null, alternatives);

        assertEquals(1, resultList.size());
        assertEquals(2, resultList.get(0).size());
    }


    @Test
    void test_getAlternativeScopes_twoTerminalsFiveNonTerminal_returnsTwoListsInList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<Sequence> alternatives = List.of(
                new Sequence(List.of(TEXT_FIRST)),
                new Sequence(List.of(ID_FIRST)),
                new Sequence(List.of(ID_SECOND)),
                new Sequence(List.of(TEXT_SECOND)),
                new Sequence(List.of(ID_SECOND)));

        Method method = GraphBuilder.class.getDeclaredMethod("getAlternativeScopes", List.class);
        method.setAccessible(true);
        final List<List<Sequence>> resultList = (List<List<Sequence>>) method.invoke(null, alternatives);

        assertEquals(2, resultList.size());
        assertEquals(3, resultList.get(0).size());
        assertEquals(2, resultList.get(1).size());
    }
}