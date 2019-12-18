package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.BnfParser;
import de.etgramlich.antlr.parser.listener.IdListener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdListenerTest {

    @Test
    void enterText_normalText_returnsTextItself() {
        final String expectedId = "some-text is here with spaces inside";

        IdListener listener = new IdListener();

        listener.enterKeyword(new KeywordMock(expectedId));
        assertEquals(expectedId, listener.getId().getText());
    }

    @Test
    void enterText_endsWithEmptySpaces_returnsTrimmed() {
        final String expectedId = "some-id-without-spaces in the end";

        IdListener listener = new IdListener();
        listener.enterKeyword(new KeywordMock(expectedId + "   "));
        assertEquals(expectedId, listener.getId().getText());
    }

    @Test
    void enterRuleid_normalId_returnsIdItself() {
        final String expectedId = "some-id";

        IdListener listener = new IdListener();

        listener.enterType(new TypeMock(expectedId));
        assertEquals(expectedId, listener.getId().getText());
    }

    @Test
    void enterRuleid_endsWithEmptySpaces_returnsTrimmed() {
        final String expectedId = "some-id-without-spaces";

        IdListener listener = new IdListener();
        listener.enterType(new TypeMock(expectedId + "   "));
        assertEquals(expectedId, listener.getId().getText());
    }

    @Test
    void enterId_idWithoutSpaces_returnsIdWithoutLTGT() {
        final String expectedId = "new-id";

        IdListener listener = new IdListener();

        listener.enterNt(new NtMock("<new-id>"));
        assertEquals(expectedId, listener.getId().getText());
    }

    @Test
    void enterId_idWithSpaces_returnsIdWithoutSpacesAndLTGT() {
        final String expectedId = "new-id";

        IdListener listener = new IdListener();

        listener.enterNt(new NtMock(" <new-id   > "));
        assertEquals(expectedId, listener.getId().getText());
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
