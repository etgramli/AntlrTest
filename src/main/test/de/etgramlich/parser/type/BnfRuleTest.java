package de.etgramlich.parser.type;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BnfRuleTest {
    private static final NonTerminal NT = new NonTerminal("NT");
    private static final Element NT_ELEMENT = new NonTerminal("NT_ELEMENT");
    private static final Element T_ELEMENT = new Type("Type_Element");
    private static final Element K_ELEMENT = new Type("Keyword_Element");
    private static final Element K2_ELEMENT = new Type("Keyword2_Element");

    @Test
    void isTerminal_onlyTerminals_returnsTrue() {
        final Sequence sequence = new Sequence(List.of(K_ELEMENT, K2_ELEMENT, T_ELEMENT));
        final BnfRule rule = new BnfRule(NT, new Alternatives(List.of(sequence)));

        assertTrue(rule.isTerminal());
    }

    @Test
    void isTerminal_oneNonTerminals_returnsFalse() {
        final Sequence sequence = new Sequence(List.of(K_ELEMENT, K2_ELEMENT, T_ELEMENT, NT_ELEMENT));
        final BnfRule rule = new BnfRule(NT, new Alternatives(List.of(sequence)));

        assertFalse(rule.isTerminal());
    }

    @Test
    void isTerminal_onlyNonTerminals_returnsFalse() {
        final Sequence sequence = new Sequence(List.of(NT_ELEMENT, NT_ELEMENT, NT_ELEMENT));
        final BnfRule rule = new BnfRule(NT, new Alternatives(List.of(sequence)));

        assertFalse(rule.isTerminal());
    }

    @Test
    void isStartRule_oneNonTerminal_returnsTrue() {
        final BnfRule startRule = new BnfRule(NT, new Alternatives(List.of(new Sequence(List.of(NT_ELEMENT)))));

        assertTrue(startRule.isStartRule());
    }

    @Test
    void isStartRule_oneTerminal_returnsFalse() {
        final BnfRule noStartRule = new BnfRule(NT, new Alternatives(List.of(new Sequence(List.of(T_ELEMENT)))));

        assertFalse(noStartRule.isStartRule());
    }

}
