parser grammar FilterParser;

options { tokenVocab=FilterLexer; }

program: 
    expr EOF
    ;


expr:
    //Level 16 Primary, array and member access
    LPAREN expr RPAREN
    | identifier op = (LE | GE | GT | LT ) literal
    | identifier op = ( EQUAL | NEQUAL ) literal      
    | expr op=AND expr                               
    | expr op=OR expr                               
;

primary: 
    LPAREN expr RPAREN
	| identifier
	| literal
	;

literal:
    INT_LITERAL;

identifier:
    ALL_KEY
    | SEVERITY_KEY '.' (SEV_TRIVIAL|SEV_MINOR|SEV_MAJOR|SEV_SEVERE|SEV_CATASTROPHIC)
    | TYPE_KEY '.' (TYPE_FIX|TYPE_PERFORMANCE|TYPE_SECURITY|TYPE_MAINTAINABILITY|TYPE_REMOVE);