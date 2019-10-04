parser grammar LanguageParser;
options { tokenVocab = LanguageLexer; }

modifier: MODIFIER;
type: TYPE;
identifier: IDENTIFIER;
declaration: (modifier)? type identifier;
