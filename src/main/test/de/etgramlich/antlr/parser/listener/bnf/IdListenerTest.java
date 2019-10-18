package de.etgramlich.antlr.parser.listener.bnf;

import de.etgramlich.antlr.parser.gen.bnf.bnfParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdListenerTest {

    @Test
    void enterRuleid_normalId_returnsIdItself() {
        final String expectedId = "some-id";

        IdListener listener = new IdListener();

        listener.enterRuleid(new RuleIdMock(expectedId));
        assertEquals(expectedId, listener.getText());
    }

    @Test
    void enterRuleid_idWithEmptySpaces_returnsTrimmed() {
        final String expectedId = "some-id-without-spaces";

        IdListener listener = new IdListener();
        listener.enterRuleid(new RuleIdMock(expectedId + "   "));
        assertEquals(expectedId, listener.getText());
    }

    @Test
    void enterId_idWithoutSpaces_returnsIdWithoutLTGT() {
        final String expectedId = "new-id";

        IdListener listener = new IdListener();

        listener.enterId(new IdMock("<new-id>"));
        assertEquals(expectedId, listener.getText());
    }

    @Test
    void enterId_idWithSpaces_returnsIdWithoutSpacesAndLTGT() {
        final String expectedId = "new-id";

        IdListener listener = new IdListener();

        listener.enterId(new IdMock(" <new-id   > "));
        assertEquals(expectedId, listener.getText());
    }


    // Mock to set a static id (text with <> around) and query that id
    private static class IdMock extends bnfParser.IdContext {
        private String text;
        IdMock(final String text) {
            super(null, 0);
            this.text = text;
        }
        @Override
        public String getText() {
            return text;
        }
    }

    // Mock for a rule id (text, and - sign), the id can be set in constructor and only be queried
    private static class RuleIdMock extends bnfParser.RuleidContext {
        private String text;
        RuleIdMock(final String text) {
            super(null, 0);
            this.text = text;
        }
        @Override
        public String getText() {
            return text;
        }
    }
}
