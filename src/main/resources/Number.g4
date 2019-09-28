grammar Number;

WHITESPACE: ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+;
BRACE_LEFT: '(' | ' ( ';
BRACE_RIGHT: ')' | ' ) ';

fragment DIGITS: '0'..'9';
NUMBER: (DIGITS)+;

OP_PLUS: '+';
OP_MINUS: '-';
OP_MULTIPLY: '*';
OP_DIVIDE: '/';

expr: term ( (OP_PLUS | OP_MINUS) term)*;
term: factor ( (OP_MULTIPLY | OP_DIVIDE) factor )*;
factor: NUMBER;
