// Generated from /home/eti/IdeaProjects/AntlrTest/src/main/resources/Number.g4 by ANTLR 4.7.2
package de.etgramlich.antlr.parser.gen.number;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class NumberLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WHITESPACE=1, BRACE_LEFT=2, BRACE_RIGHT=3, NUMBER=4, OP_PLUS=5, OP_MINUS=6, 
		OP_MULTIPLY=7, OP_DIVIDE=8;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WHITESPACE", "BRACE_LEFT", "BRACE_RIGHT", "DIGITS", "NUMBER", "OP_PLUS", 
			"OP_MINUS", "OP_MULTIPLY", "OP_DIVIDE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'+'", "'-'", "'*'", "'/'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WHITESPACE", "BRACE_LEFT", "BRACE_RIGHT", "NUMBER", "OP_PLUS", 
			"OP_MINUS", "OP_MULTIPLY", "OP_DIVIDE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
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
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public NumberLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Number.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\n\65\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\6"+
		"\2\27\n\2\r\2\16\2\30\3\3\3\3\3\3\3\3\5\3\37\n\3\3\4\3\4\3\4\3\4\5\4%"+
		"\n\4\3\5\3\5\3\6\6\6*\n\6\r\6\16\6+\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\2"+
		"\2\13\3\3\5\4\7\5\t\2\13\6\r\7\17\b\21\t\23\n\3\2\3\5\2\13\f\16\17\"\""+
		"\2\67\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17"+
		"\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\3\26\3\2\2\2\5\36\3\2\2\2\7$\3\2\2"+
		"\2\t&\3\2\2\2\13)\3\2\2\2\r-\3\2\2\2\17/\3\2\2\2\21\61\3\2\2\2\23\63\3"+
		"\2\2\2\25\27\t\2\2\2\26\25\3\2\2\2\27\30\3\2\2\2\30\26\3\2\2\2\30\31\3"+
		"\2\2\2\31\4\3\2\2\2\32\37\7*\2\2\33\34\7\"\2\2\34\35\7*\2\2\35\37\7\""+
		"\2\2\36\32\3\2\2\2\36\33\3\2\2\2\37\6\3\2\2\2 %\7+\2\2!\"\7\"\2\2\"#\7"+
		"+\2\2#%\7\"\2\2$ \3\2\2\2$!\3\2\2\2%\b\3\2\2\2&\'\4\62;\2\'\n\3\2\2\2"+
		"(*\5\t\5\2)(\3\2\2\2*+\3\2\2\2+)\3\2\2\2+,\3\2\2\2,\f\3\2\2\2-.\7-\2\2"+
		".\16\3\2\2\2/\60\7/\2\2\60\20\3\2\2\2\61\62\7,\2\2\62\22\3\2\2\2\63\64"+
		"\7\61\2\2\64\24\3\2\2\2\7\2\30\36$+\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}