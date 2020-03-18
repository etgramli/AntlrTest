package de.etgramlich.dsl.parser.listener;

import de.etgramlich.dsl.parser.gen.bnf.BnfLexer;
import de.etgramlich.dsl.parser.gen.bnf.BnfParser;
import de.etgramlich.dsl.parser.type.Bnf;
import de.etgramlich.dsl.parser.type.BnfRule;
import de.etgramlich.dsl.parser.type.Alternatives;
import de.etgramlich.dsl.parser.type.Sequence;
import de.etgramlich.dsl.parser.type.Element;
import de.etgramlich.dsl.parser.type.repetition.Optional;
import de.etgramlich.dsl.parser.type.repetition.Precedence;
import de.etgramlich.dsl.parser.type.repetition.ZeroOrMore;
import de.etgramlich.dsl.parser.type.text.Keyword;
import de.etgramlich.dsl.parser.type.text.TextElement;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BnfListenerTest {

    private static final String GRAMMAR_ALTERNATIVE = "<nonterminal> = 'alternativeA' | 'alternativeB';";
    private static final String GRAMMAR_ZEROORMORE = "<nonterminal> = {'alternativeA' 'alternativeB'};";
    private static final String GRAMMAR_OPTIONAL = "<nonterminal> = ['alternativeA' 'alternativeB'];";
    private static final String GRAMMAR_PRECEDENCE = "<nonterminal> = ('alternativeA' 'alternativeB');";
    private static final String GRAMMAR_PRECEDENCE_ALTERNATIVE = "<nonterminal> = ('alternativeA' | 'alternativeB');";
    private static final TextElement ALT_A = new Keyword("alternativeA");
    private static final TextElement ALT_B = new Keyword("alternativeB");

    @Test
    void enterBnf_ruleWithTwoAlternatives() {
        final BnfListener listener = new BnfListener();
        listener.enterBnf(new BnfParser(new CommonTokenStream(new BnfLexer(CharStreams.fromString(GRAMMAR_ALTERNATIVE)))).bnf());

        final Bnf bnf = listener.getBnf();

        assertEquals(1, bnf.getBnfRules().size());
        final BnfRule rule = bnf.getBnfRules().get(0);
        assertEquals("nonterminal", rule.getName());
        final Alternatives rhs = rule.getRhs();
        assertEquals(2, rhs.getSequences().size());
        for (Sequence sequence : rhs.getSequences()) {
            assertEquals(1, sequence.getElements().size());
            assertTrue(sequence.getElements().get(0).equals(ALT_A) || sequence.getElements().get(0).equals(ALT_B));
        }
    }

    @Test
    void enterBnf_ruleWithZeroOrMore() {
        final BnfListener listener = new BnfListener();
        listener.enterBnf(new BnfParser(new CommonTokenStream(new BnfLexer(CharStreams.fromString(GRAMMAR_ZEROORMORE)))).bnf());

        final Bnf bnf = listener.getBnf();

        assertEquals(1, bnf.getBnfRules().size());
        final BnfRule rule = bnf.getBnfRules().get(0);
        assertEquals("nonterminal", rule.getName());
        final Alternatives rhs = rule.getRhs();
        assertEquals(1, rhs.getSequences().size());
        final Sequence sequence = rhs.getSequences().get(0);
        assertEquals(1, sequence.getElements().size());
        final Element element = sequence.getElements().get(0);
        assertTrue(element instanceof ZeroOrMore);
        final ZeroOrMore zeroOrMore = (ZeroOrMore) element;
        assertEquals(2, zeroOrMore.getAlternatives().getElements().size());
        final List<Element> elements = zeroOrMore.getAlternatives().getElements();
        assertEquals(List.of(ALT_A, ALT_B), elements);
    }

    @Test
    void enterBnf_ruleWithOptional() {
        final BnfListener listener = new BnfListener();
        listener.enterBnf(new BnfParser(new CommonTokenStream(new BnfLexer(CharStreams.fromString(GRAMMAR_OPTIONAL)))).bnf());

        final Bnf bnf = listener.getBnf();

        assertEquals(1, bnf.getBnfRules().size());
        final BnfRule rule = bnf.getBnfRules().get(0);
        assertEquals("nonterminal", rule.getName());
        final Alternatives rhs = rule.getRhs();
        assertEquals(1, rhs.getSequences().size());
        final Sequence sequence = rhs.getSequences().get(0);
        assertEquals(1, sequence.getElements().size());
        final Element element = sequence.getElements().get(0);
        assertTrue(element instanceof Optional);
        final Optional zeroOrMore = (Optional) element;
        assertEquals(2, zeroOrMore.getAlternatives().getElements().size());
        final List<Element> elements = zeroOrMore.getAlternatives().getElements();
        assertEquals(List.of(ALT_A, ALT_B), elements);
    }

    @Test
    void enterBnf_ruleWithPrecedence() {
        final BnfListener listener = new BnfListener();
        listener.enterBnf(new BnfParser(new CommonTokenStream(new BnfLexer(CharStreams.fromString(GRAMMAR_PRECEDENCE)))).bnf());

        final Bnf bnf = listener.getBnf();

        assertEquals(1, bnf.getBnfRules().size());
        final BnfRule rule = bnf.getBnfRules().get(0);
        assertEquals("nonterminal", rule.getName());
        final Alternatives rhs = rule.getRhs();
        assertEquals(1, rhs.getSequences().size());
        final Sequence sequence = rhs.getSequences().get(0);
        assertEquals(1, sequence.getElements().size());
        final Element element = sequence.getElements().get(0);
        assertTrue(element instanceof Precedence);
        final Precedence zeroOrMore = (Precedence) element;
        assertEquals(2, zeroOrMore.getAlternatives().getElements().size());
        final List<Element> elements = zeroOrMore.getAlternatives().getElements();
        assertEquals(List.of(ALT_A, ALT_B), elements);
    }

    @Test
    void enterBnf_ruleWithPrecedenceAndInnerAlternative() {
        final BnfListener listener = new BnfListener();
        listener.enterBnf(new BnfParser(new CommonTokenStream(new BnfLexer(CharStreams.fromString(GRAMMAR_PRECEDENCE_ALTERNATIVE)))).bnf());

        final Bnf bnf = listener.getBnf();

        assertEquals(1, bnf.getBnfRules().size());
        final BnfRule rule = bnf.getBnfRules().get(0);
        assertEquals("nonterminal", rule.getName());
        final Alternatives rhs = rule.getRhs();
        assertEquals(1, rhs.getSequences().size());
        final Sequence sequence = rhs.getSequences().get(0);
        assertEquals(1, sequence.getElements().size());
        final Element element = sequence.getElements().get(0);
        assertTrue(element instanceof Precedence);
        final Precedence zeroOrMore = (Precedence) element;
        final List<Sequence> subSequence = zeroOrMore.getAlternatives().getSequences();
        assertEquals(2, subSequence.size());
        for (Sequence s : subSequence) {
            assertEquals(1, s.getElements().size());
            assertTrue(s.getElements().get(0).equals(ALT_A) || s.getElements().get(0).equals(ALT_B));
        }
    }
}
