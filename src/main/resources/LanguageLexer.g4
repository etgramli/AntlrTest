lexer grammar LanguageLexer;

channels {
  WHITESPACE_CHANNEL,
  COMMENTS_CHANNEL
}

MODIFIER: 'unsigned' | 'signed';
TYPE: 'int' | 'long';
IDENTIFIER: ('a'..'z')+;

WS : [ \r\t\n]+ -> channel(WHITESPACE_CHANNEL) ;
