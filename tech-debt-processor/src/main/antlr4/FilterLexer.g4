lexer grammar FilterLexer;

options {
    caseInsensitive = true;
}

SEVERITY_KEY    : 'severity';
TYPE_KEY        : 'type';
EFFORT_TYPE     : 'effort';
COMMENT_KET     : 'comment';
SOLUTION_KEY    : 'solution';
AUTHOR_KEY      : 'author';

// Separators
LPAREN : '(';
RPAREN : ')';

// Operators
EQUAL    : '=';
NEQUAL : '!=';
GT       : '>';
LT       : '<';
LE       : '<=';
GE       : '>=';
LIKE     : 'like';
IN       : 'in';
NOT      : 'not';

AND     : 'and';
OR      : 'or';

COMMA   : ',';
STRING_LITERAL_APEX: '\'' (~['\\\r\n] | EscapeSequence)* '\'';

STRING_LITERAL_DA: '"' (~["\\\r\n] | EscapeSequence)* '"';

WS           : [ \t\r\n\u000C]+ -> channel(HIDDEN);

fragment EscapeSequence:
    '\\' 'u005c'? [btnfr"'\\]
    | '\\' 'u005c'? ([0-3]? [0-7])? [0-7]
    | '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
;

fragment HexDigits: HexDigit ((HexDigit | '_')* HexDigit)?;

fragment HexDigit: [0-9A-F];

fragment Digits: [0-9] ([0-9_]* [0-9])?;
