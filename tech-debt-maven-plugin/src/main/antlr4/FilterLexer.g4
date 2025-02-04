lexer grammar FilterLexer;

options {
    caseInsensitive = true;
}

ALL_KEY         : 'all';
SEVERITY_KEY    : 'severity';
TYPE_KEY        : 'type';


SEV_TRIVIAL     : 'TRIVIAL';
SEV_MINOR       : 'MINOR';
SEV_MAJOR       : 'MAJOR';
SEV_SEVERE      : 'SEVERE';
SEV_CATASTROPHIC: 'CATASTROPHIC';

TYPE_FIX            : 'FIX';
TYPE_PERFORMANCE    : 'PERFORMANCE';
TYPE_SECURITY       : 'SECURITY';
TYPE_MAINTAINABILITY: 'MAINTAINABILITY';
TYPE_REMOVE         : 'REMOVE';

DOT     : '.';

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

AND     : 'and';
OR      : 'or';

WS           : [ \t\r\n\u000C]+ -> channel(HIDDEN);

INT_LITERAL: Digits;

fragment EscapeSequence:
    '\\' 'u005c'? [btnfr"'\\]
    | '\\' 'u005c'? ([0-3]? [0-7])? [0-7]
    | '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
;

fragment HexDigits: HexDigit ((HexDigit | '_')* HexDigit)?;

fragment HexDigit: [0-9A-F];

fragment Digits: [0-9] ([0-9_]* [0-9])?;
