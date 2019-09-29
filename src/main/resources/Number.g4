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

expr: term ( (operation_expr) term)*;
operation_expr: (OP_PLUS | OP_MINUS);
term: factor ( (operation_term) factor )*;
operation_term: (OP_MULTIPLY | OP_DIVIDE);
factor: NUMBER;
