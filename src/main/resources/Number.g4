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

expression: expr EOF;
expr: left=expr operation_expr right=term
    | right=term;
operation_expr: (OP_PLUS | OP_MINUS);
term: left=term (operation_term) right=factor
      | right=factor;
operation_term: (OP_MULTIPLY | OP_DIVIDE);
factor: NUMBER;
