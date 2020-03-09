// Generated from /home/eti/IdeaProjects/AntlrTest/src/main/resources/Bnf.g4 by ANTLR 4.8
package de.etgramlich.dsl.parser.gen.bnf;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.Arrays;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BnfLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION);
    }

    private static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            NT = 10, KEYWORD = 11, TYPE = 12, WS = 13;
    private final static String[] channelNames = {"DEFAULT_TOKEN_CHANNEL", "HIDDEN"};

    private final static String[] modeNames = {"DEFAULT_MODE"};

    private static final String[] ruleNames = new String[]{
            "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", "NT", "KEYWORD", "TYPE", "WS"
    };

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

    public BnfLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
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
    public String[] getChannelNames() {
        return Arrays.copyOf(channelNames, channelNames.length);
    }

    @Override
    public String[] getModeNames() {
        return Arrays.copyOf(modeNames, modeNames.length);
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\17I\b\1\4\2\t\2\4" +
                    "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
                    "\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6" +
                    "\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\6\13\63\n\13\r\13\16\13" +
                    "\64\3\13\3\13\3\f\3\f\6\f;\n\f\r\f\16\f<\3\f\3\f\3\r\6\rB\n\r\r\r\16\r" +
                    "C\3\16\3\16\3\16\3\16\2\2\17\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13" +
                    "\25\f\27\r\31\16\33\17\3\2\4\4\2C\\c|\5\2\13\f\17\17\"\"\2K\2\3\3\2\2" +
                    "\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3" +
                    "\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2" +
                    "\2\2\33\3\2\2\2\3\35\3\2\2\2\5\37\3\2\2\2\7!\3\2\2\2\t#\3\2\2\2\13%\3" +
                    "\2\2\2\r\'\3\2\2\2\17)\3\2\2\2\21+\3\2\2\2\23-\3\2\2\2\25/\3\2\2\2\27" +
                    "8\3\2\2\2\31A\3\2\2\2\33E\3\2\2\2\35\36\7?\2\2\36\4\3\2\2\2\37 \7=\2\2" +
                    " \6\3\2\2\2!\"\7~\2\2\"\b\3\2\2\2#$\7]\2\2$\n\3\2\2\2%&\7_\2\2&\f\3\2" +
                    "\2\2\'(\7}\2\2(\16\3\2\2\2)*\7\177\2\2*\20\3\2\2\2+,\7*\2\2,\22\3\2\2" +
                    "\2-.\7+\2\2.\24\3\2\2\2/\60\7>\2\2\60\62\4c|\2\61\63\t\2\2\2\62\61\3\2" +
                    "\2\2\63\64\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65\66\3\2\2\2\66\67\7@" +
                    "\2\2\67\26\3\2\2\28:\7)\2\29;\t\2\2\2:9\3\2\2\2;<\3\2\2\2<:\3\2\2\2<=" +
                    "\3\2\2\2=>\3\2\2\2>?\7)\2\2?\30\3\2\2\2@B\t\2\2\2A@\3\2\2\2BC\3\2\2\2" +
                    "CA\3\2\2\2CD\3\2\2\2D\32\3\2\2\2EF\t\3\2\2FG\3\2\2\2GH\b\16\2\2H\34\3" +
                    "\2\2\2\6\2\64<C\3\b\2\2";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}