package de.etgramlich.parser.listener;

import de.etgramlich.parser.gen.bnf.BnfParser;
import de.etgramlich.parser.listener.NonTerminalListener;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class IdListenerTest {

    @Test
    void enterText_normalText_returnsTextItself() {
        final String expectedId = "some-text is here with spaces inside";

        NonTerminalListener listener = new NonTerminalListener();

        listener.enterKeyword(new KeywordMock(expectedId));
        assertEquals(expectedId, listener.getTextElement().getName());
    }

    @Test
    void enterText_endsWithEmptySpaces_returnsTrimmed() {
        final String expectedId = "some-id-without-spaces in the end";

        NonTerminalListener listener = new NonTerminalListener();
        listener.enterKeyword(new KeywordMock(expectedId + "   "));
        assertEquals(expectedId, listener.getTextElement().getName());
    }

    @Test
    void enterRuleid_normalId_returnsIdItself() {
        final String expectedId = "some-id";

        NonTerminalListener listener = new NonTerminalListener();

        listener.enterKeyword(new KeywordMock(expectedId));
        assertEquals(expectedId, listener.getTextElement().getName());
    }

    @Test
    void enterRuleid_endsWithEmptySpaces_returnsTrimmed() {
        final String expectedId = "some-id-without-spaces";

        NonTerminalListener listener = new NonTerminalListener();
        listener.enterType(new TypeMock(expectedId + "   "));
        assertEquals(expectedId, listener.getTextElement().getName());
    }

    @Test
    void enterId_idWithoutSpaces_returnsIdWithoutLTGT() {
        final String expectedId = "new-id";

        NonTerminalListener listener = new NonTerminalListener();

        listener.enterNt(new NtMock("<new-id>"));
        assertEquals(expectedId, listener.getTextElement().getName());
    }

    @Test
    void enterId_idWithSpaces_returnsIdWithoutSpacesAndLTGT() {
        final String expectedId = "new-id";

        NonTerminalListener listener = new NonTerminalListener();

        listener.enterNt(new NtMock(" <new-id   > "));
        assertEquals(expectedId, listener.getTextElement().getName());
    }


    // Mock to set a static text (characters and - sign and spaces), set text in constructor and query only afterwards
    private static class KeywordMock extends BnfParser.KeywordContext {
        private final String text;
        KeywordMock(final String text) {
            super(null, 0);
            this.text = text;
        }
        @Override
        public String getText() {
            return text;
        }
    }

    // Mock to set a static id (text with <> around) and query that id
    private static class NtMock extends BnfParser.NtContext {
        private final String text;
        NtMock(final String text) {
            super(null, 0);
            this.text = text;
        }
        @Override
        public String getText() {
            return text;
        }
    }

    // Mock for a rule id (text, and - sign), the id can be set in constructor and only be queried
    private static class TypeMock extends BnfParser.TypeContext {
        private final String text;
        TypeMock(final String text) {
            super(null, 0);
            this.text = text;
        }
        @Override
        public String getText() {
            return text;
        }
    }
}
