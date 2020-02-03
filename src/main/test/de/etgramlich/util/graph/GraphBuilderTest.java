package de.etgramlich.util.graph;

import de.etgramlich.parser.type.text.Keyword;
import de.etgramlich.parser.type.text.NonTerminal;
import de.etgramlich.parser.type.Sequence;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
}