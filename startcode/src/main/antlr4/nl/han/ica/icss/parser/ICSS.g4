grammar ICSS;

stylesheet: variableAssignment* styleRule* EOF;

selector: (tagSelector | classSelector | idSelector) (COMMA selector)*;
classSelector: CLASS_IDENT;
tagSelector: LOWER_IDENT;
idSelector: ID_IDENT | COLOR;

styleRule: selector OPEN_BRACE ruleBody CLOSE_BRACE;
propertyName: LOWER_IDENT;

ruleBody: (declaration | booleanClause | variableAssignment)*;
declaration: propertyName COLON expression SEMICOLON;
booleanClause: IF BOX_BRACKET_OPEN (variableReference | boolLiteral) BOX_BRACKET_CLOSE OPEN_BRACE ruleBody CLOSE_BRACE elseClause?;

IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
CLASS_IDENT: '.' [a-z0-9\-]+;
ID_IDENT: '#' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COMMA : ',';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

variableAssignment: variableReference ASSIGNMENT_OPERATOR expression SEMICOLON;

elseClause: ELSE OPEN_BRACE ruleBody CLOSE_BRACE;

expression: literal | expression (MUL | DIV) expression | expression (PLUS | MIN) expression;

boolLiteral: TRUE | FALSE;
colorLiteral: COLOR;
percentageLiteral: PERCENTAGE;
pixelLiteral: PIXELSIZE;
scalarLiteral: SCALAR;
variableReference: CAPITAL_IDENT;
literal: boolLiteral | colorLiteral | percentageLiteral | pixelLiteral | scalarLiteral | variableReference;
