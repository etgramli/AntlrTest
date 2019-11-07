<joi> ::= <component>
<component> ::= 'component' <name> 'implements' <componentInterface> {<componentInterface>} (<componentMethod>) {<componentField>}
<componentInterface> ::= <name>
<componentMethod> ::= <name>
<componentField> ::= <name>
<name> ::= 'A'..'Z' {'a'..'z'|'A'..'Z'|'0'..'9'|'_'}
