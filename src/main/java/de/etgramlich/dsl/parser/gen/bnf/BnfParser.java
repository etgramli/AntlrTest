// Generated from /home/eti/IdeaProjects/AntlrTest/src/main/resources/Bnf.g4 by ANTLR 4.8
package de.etgramlich.dsl.parser.gen.bnf;

import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BnfParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION);
    }

    private static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            NT = 10, KEYWORD = 11, TYPE = 12, WS = 13;
    public static final int RULE_bnf = 0, RULE_bnfrule = 1, RULE_lhs = 2, RULE_rhs = 3, RULE_alternatives = 4,
            RULE_sequence = 5, RULE_element = 6, RULE_optional = 7, RULE_zeroormore = 8, RULE_precedence = 9,
            RULE_nt = 10, RULE_keyword = 11, RULE_type = 12;

    private static final String[] ruleNames = new String[]{"bnf", "bnfrule", "lhs", "rhs", "alternatives", "sequence",
            "element", "optional", "zeroormore", "precedence", "nt", "keyword", "type"};

    private static String[] makeLiteralNames() {
        return new String[]{null, "'='", "';'", "'|'", "'['", "']'", "'{'", "'}'", "'('", "')'"};
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[]{null, null, null, null, null, null, null, null, null, null, "NT", "KEYWORD", "TYPE", "WS"};
    }

    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    private static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return Arrays.copyOf(tokenNames, tokenNames.length);
    }

    @Override
    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "Bnf.g4";
    }

    @Override
    public String[] getRuleNames() {
        return Arrays.copyOf(ruleNames, ruleNames.length);
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public BnfParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class BnfContext extends ParserRuleContext {
        public TerminalNode EOF() {
            return getToken(BnfParser.EOF, 0);
        }

        public List<BnfruleContext> bnfrule() {
            return getRuleContexts(BnfruleContext.class);
        }

        public BnfruleContext bnfrule(int i) {
            return getRuleContext(BnfruleContext.class, i);
        }

        public BnfContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_bnf;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterBnf(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitBnf(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitBnf(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BnfContext bnf() throws RecognitionException {
        BnfContext _localctx = new BnfContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_bnf);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(29);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NT) {
                    {
                        {
                            setState(26);
                            bnfrule();
                        }
                    }
                    setState(31);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(32);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BnfruleContext extends ParserRuleContext {
        public LhsContext lhs() {
            return getRuleContext(LhsContext.class, 0);
        }

        public RhsContext rhs() {
            return getRuleContext(RhsContext.class, 0);
        }

        public BnfruleContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_bnfrule;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterBnfrule(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitBnfrule(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitBnfrule(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BnfruleContext bnfrule() throws RecognitionException {
        BnfruleContext _localctx = new BnfruleContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_bnfrule);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(34);
                lhs();
                setState(35);
                match(T__0);
                setState(36);
                rhs();
                setState(37);
                match(T__1);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class LhsContext extends ParserRuleContext {
        public NtContext nt() {
            return getRuleContext(NtContext.class, 0);
        }

        public LhsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_lhs;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterLhs(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitLhs(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitLhs(this);
            else return visitor.visitChildren(this);
        }
    }

    public final LhsContext lhs() throws RecognitionException {
        LhsContext _localctx = new LhsContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_lhs);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(39);
                nt();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class RhsContext extends ParserRuleContext {
        public AlternativesContext alternatives() {
            return getRuleContext(AlternativesContext.class, 0);
        }

        public RhsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_rhs;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterRhs(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitRhs(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitRhs(this);
            else return visitor.visitChildren(this);
        }
    }

    public final RhsContext rhs() throws RecognitionException {
        RhsContext _localctx = new RhsContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_rhs);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(41);
                alternatives();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class AlternativesContext extends ParserRuleContext {
        public List<SequenceContext> sequence() {
            return getRuleContexts(SequenceContext.class);
        }

        public SequenceContext sequence(int i) {
            return getRuleContext(SequenceContext.class, i);
        }

        public AlternativesContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_alternatives;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterAlternatives(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitAlternatives(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitAlternatives(this);
            else return visitor.visitChildren(this);
        }
    }

    public final AlternativesContext alternatives() throws RecognitionException {
        AlternativesContext _localctx = new AlternativesContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_alternatives);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(43);
                sequence();
                setState(48);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__2) {
                    {
                        {
                            setState(44);
                            match(T__2);
                            setState(45);
                            sequence();
                        }
                    }
                    setState(50);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SequenceContext extends ParserRuleContext {
        public List<ElementContext> element() {
            return getRuleContexts(ElementContext.class);
        }

        public ElementContext element(int i) {
            return getRuleContext(ElementContext.class, i);
        }

        public SequenceContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_sequence;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterSequence(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitSequence(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitSequence(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SequenceContext sequence() throws RecognitionException {
        SequenceContext _localctx = new SequenceContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_sequence);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(52);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(51);
                            element();
                        }
                    }
                    setState(54);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << NT) | (1L << KEYWORD) | (1L << TYPE))) != 0));
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ElementContext extends ParserRuleContext {
        public NtContext nt() {
            return getRuleContext(NtContext.class, 0);
        }

        public KeywordContext keyword() {
            return getRuleContext(KeywordContext.class, 0);
        }

        public TypeContext type() {
            return getRuleContext(TypeContext.class, 0);
        }

        public OptionalContext optional() {
            return getRuleContext(OptionalContext.class, 0);
        }

        public ZeroormoreContext zeroormore() {
            return getRuleContext(ZeroormoreContext.class, 0);
        }

        public PrecedenceContext precedence() {
            return getRuleContext(PrecedenceContext.class, 0);
        }

        public ElementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_element;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterElement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitElement(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitElement(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ElementContext element() throws RecognitionException {
        ElementContext _localctx = new ElementContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_element);
        try {
            setState(62);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case NT:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(56);
                    nt();
                }
                break;
                case KEYWORD:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(57);
                    keyword();
                }
                break;
                case TYPE:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(58);
                    type();
                }
                break;
                case T__3:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(59);
                    optional();
                }
                break;
                case T__5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(60);
                    zeroormore();
                }
                break;
                case T__7:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(61);
                    precedence();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class OptionalContext extends ParserRuleContext {
        public AlternativesContext alternatives() {
            return getRuleContext(AlternativesContext.class, 0);
        }

        public OptionalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_optional;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterOptional(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitOptional(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitOptional(this);
            else return visitor.visitChildren(this);
        }
    }

    public final OptionalContext optional() throws RecognitionException {
        OptionalContext _localctx = new OptionalContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_optional);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(64);
                match(T__3);
                setState(65);
                alternatives();
                setState(66);
                match(T__4);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ZeroormoreContext extends ParserRuleContext {
        public AlternativesContext alternatives() {
            return getRuleContext(AlternativesContext.class, 0);
        }

        public ZeroormoreContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_zeroormore;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterZeroormore(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitZeroormore(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitZeroormore(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ZeroormoreContext zeroormore() throws RecognitionException {
        ZeroormoreContext _localctx = new ZeroormoreContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_zeroormore);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(68);
                match(T__5);
                setState(69);
                alternatives();
                setState(70);
                match(T__6);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class PrecedenceContext extends ParserRuleContext {
        public AlternativesContext alternatives() {
            return getRuleContext(AlternativesContext.class, 0);
        }

        public PrecedenceContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_precedence;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterPrecedence(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitPrecedence(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitPrecedence(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PrecedenceContext precedence() throws RecognitionException {
        PrecedenceContext _localctx = new PrecedenceContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_precedence);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(72);
                match(T__7);
                setState(73);
                alternatives();
                setState(74);
                match(T__8);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class NtContext extends ParserRuleContext {
        public TerminalNode NT() {
            return getToken(BnfParser.NT, 0);
        }

        public NtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_nt;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterNt(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitNt(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitNt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NtContext nt() throws RecognitionException {
        NtContext _localctx = new NtContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_nt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(76);
                match(NT);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class KeywordContext extends ParserRuleContext {
        public TerminalNode KEYWORD() {
            return getToken(BnfParser.KEYWORD, 0);
        }

        public KeywordContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_keyword;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterKeyword(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitKeyword(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitKeyword(this);
            else return visitor.visitChildren(this);
        }
    }

    public final KeywordContext keyword() throws RecognitionException {
        KeywordContext _localctx = new KeywordContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_keyword);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(78);
                match(KEYWORD);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class TypeContext extends ParserRuleContext {
        public TerminalNode TYPE() {
            return getToken(BnfParser.TYPE, 0);
        }

        public TypeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_type;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).enterType(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BnfListener) ((BnfListener) listener).exitType(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof BnfVisitor) return ((BnfVisitor<? extends T>) visitor).visitType(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TypeContext type() throws RecognitionException {
        TypeContext _localctx = new TypeContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_type);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(80);
                match(TYPE);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\17U\4\2\t\2\4\3\t" +
                    "\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4" +
                    "\f\t\f\4\r\t\r\4\16\t\16\3\2\7\2\36\n\2\f\2\16\2!\13\2\3\2\3\2\3\3\3\3" +
                    "\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\7\6\61\n\6\f\6\16\6\64\13\6\3" +
                    "\7\6\7\67\n\7\r\7\16\78\3\b\3\b\3\b\3\b\3\b\3\b\5\bA\n\b\3\t\3\t\3\t\3" +
                    "\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16" +
                    "\2\2\17\2\4\6\b\n\f\16\20\22\24\26\30\32\2\2\2O\2\37\3\2\2\2\4$\3\2\2" +
                    "\2\6)\3\2\2\2\b+\3\2\2\2\n-\3\2\2\2\f\66\3\2\2\2\16@\3\2\2\2\20B\3\2\2" +
                    "\2\22F\3\2\2\2\24J\3\2\2\2\26N\3\2\2\2\30P\3\2\2\2\32R\3\2\2\2\34\36\5" +
                    "\4\3\2\35\34\3\2\2\2\36!\3\2\2\2\37\35\3\2\2\2\37 \3\2\2\2 \"\3\2\2\2" +
                    "!\37\3\2\2\2\"#\7\2\2\3#\3\3\2\2\2$%\5\6\4\2%&\7\3\2\2&\'\5\b\5\2\'(\7" +
                    "\4\2\2(\5\3\2\2\2)*\5\26\f\2*\7\3\2\2\2+,\5\n\6\2,\t\3\2\2\2-\62\5\f\7" +
                    "\2./\7\5\2\2/\61\5\f\7\2\60.\3\2\2\2\61\64\3\2\2\2\62\60\3\2\2\2\62\63" +
                    "\3\2\2\2\63\13\3\2\2\2\64\62\3\2\2\2\65\67\5\16\b\2\66\65\3\2\2\2\678" +
                    "\3\2\2\28\66\3\2\2\289\3\2\2\29\r\3\2\2\2:A\5\26\f\2;A\5\30\r\2<A\5\32" +
                    "\16\2=A\5\20\t\2>A\5\22\n\2?A\5\24\13\2@:\3\2\2\2@;\3\2\2\2@<\3\2\2\2" +
                    "@=\3\2\2\2@>\3\2\2\2@?\3\2\2\2A\17\3\2\2\2BC\7\6\2\2CD\5\n\6\2DE\7\7\2" +
                    "\2E\21\3\2\2\2FG\7\b\2\2GH\5\n\6\2HI\7\t\2\2I\23\3\2\2\2JK\7\n\2\2KL\5" +
                    "\n\6\2LM\7\13\2\2M\25\3\2\2\2NO\7\f\2\2O\27\3\2\2\2PQ\7\r\2\2Q\31\3\2" +
                    "\2\2RS\7\16\2\2S\33\3\2\2\2\6\37\628@";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}