grammar Bnf;

bnf
    : bnfrule* EOF
    ;

bnfrule
    : lhs '=' rhs ';'
    ;

lhs
    : nt
    ;

rhs
    : alternatives
    ;

alternatives
    : sequence ('|' sequence)*
    ;

sequence
    : element+
    ;

element
    : nt
    | keyword
    | type
    | optional
    | zeroormore
    | precedence
    ;

optional
    : '[' alternatives ']'
    ;

zeroormore
    : '{' alternatives '}'
    ;

precedence
    : '(' alternatives ')'
    ;

nt
    : NT
    ;
keyword
    : KEYWORD
    ;

type
    : TYPE
    ;

NT
    : '<'('a'..'z')('a'..'z'|'A'..'Z')+'>'
    ;

KEYWORD
    : '\''('a'..'z'|'A'..'Z')+'\''
    ;

TYPE
    : ('a'..'z'|'A'..'Z')+
    ;

WS
    : [ \r\n\t] -> skip
    ;
