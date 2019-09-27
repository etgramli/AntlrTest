grammar Number;

digit: '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' | '0';
positive_number: digit+;
number: '-'positive_number | positive_number;

op_plus: '+' | ' + ';
op_minus: '-' | ' - ';
op_multiply: '*' | ' * ';
op_divide: '/' | ' / ';
operation_kind: op_plus | op_minus | op_multiply | op_divide;

brack_left: '(' | ' ( ';
brack_right: ')' | ' ) ';
operation: number operation_kind number
         | brack_left operation brack_right
         | number operation_kind operation
         | operation operation_kind number;
