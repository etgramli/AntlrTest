// Joi.g4
grammar Joi;

joi
    :    component
    ;

component
    :    'component' NAME
         'implements' componentInterface (',' componentInterface)* '{'
             componentMethod+
             componentField*
         '}'
    ;

componentInterface
    :    NAME
    ;

componentMethod
    :    METHOD
    ;

componentField
    :    FIELD
    ;

METHOD : '@method' .*? '@end' ;

FIELD : '@field' .*? '@end' ;

NAME : ('A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

NEWLINE : '\r'? '\n' -> skip ;

WHITESPACE : [ \t]+ -> skip ;

