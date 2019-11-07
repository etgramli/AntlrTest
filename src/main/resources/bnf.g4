/*
 [The 'BSD licence']
 Copyright (c) 2013 Tom Everett
 All rights reserved.
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.
 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

// ToDo: Change grammar to non-left-recursive

grammar bnf;

rulelist
    : rule_* EOF
;

rule_
    : lhs ASSIGN rhs
    ;

lhs
    : id
    ;

rhs
    : alternatives
    ;

alternatives    // Alternative
    : alternative (BAR alternative)*
    ;

alternative     // Sequence
    : element*
    ;

element
    : optional  // \
    | zeroormore//  }= Repetition
    | oneormore // /
    | text
    | id
    | letterrange
    ;

optional
    : REND alternatives LEND
    ;

zeroormore
    : RBRACE alternatives LBRACE
    ;

oneormore
    : RPAREN alternatives LPAREN
    ;

text
    : TEXT
    ;

id
    : LT ruleid GT
    ;

ruleid
    : ID
    ;

letterrange
    : LETTERRANGE
    ;

ASSIGN
    : '::='
    ;

LPAREN
    : ')'
    ;

RPAREN
    : '('
    ;

LBRACE
    : '}'
    ;

RBRACE
    : '{'
    ;

LEND
    : ']'
    ;

REND
    : '['
    ;

BAR
    : '|'
    ;

GT
    : '>'
    ;

LT
    : '<'
    ;

TEXT
    : '\''('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|':'|'-'|'_'|' ')*'\''
    ;

ID
    : ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'-'|' '|'_')+
    ;

LETTERRANGE
    : LETTER '..' LETTER
    | DIGIT '..' DIGIT
    ;

LETTER
    : '\'' ('A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G' | 'H' | 'I' | 'J' | 'K' | 'L' | 'M' | 'N' | 'O' | 'P' | 'Q' | 'R' | 'S' | 'T' | 'U' | 'V' | 'W' | 'X' | 'Y' | 'Z' | 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i' | 'j' | 'k' | 'l' | 'm' | 'n' | 'o' | 'p' | 'q' | 'r' | 's' | 't' | 'u' | 'v' | 'w' | 'x' | 'y' | 'z') '\''
    ;

DIGIT
    : '\'' ('0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9') '\''
    ;

WS
    : [ \r\n\t] -> skip
    ;
