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
    | identifier 
        (neg = NOT )?
        op = LIKE primary
    | identifier 
        (neg = NOT )?
        op = IN '(' literal (',' literal)* ')'
    | expr op=AND expr                               
    | expr op=OR expr                               
;

primary: 
    LPAREN expr RPAREN
	| identifier
	| stringLiteral
	;

literal:
    stringLiteral;

stringLiteral:
	STRING_LITERAL_APEX
	| STRING_LITERAL_DA;

identifier:
    SEVERITY_KEY
    | TYPE_KEY
    | EFFORT_TYPE
    | COMMENT_KET
    | SOLUTION_KEY
    | AUTHOR_KEY
    ;