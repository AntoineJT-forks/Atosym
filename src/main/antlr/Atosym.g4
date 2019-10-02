grammar Atosym;

main: exp;

exp
    : func OPENING_BRACKET series CLOSING_BRACKET
    | OPENING_BRACKET exp CLOSING_BRACKET
    | exp OTHER_SYMBOL
    | exp non_lit_func[3] exp
    | exp non_lit_func[2] exp
    | exp non_lit_func[1] exp
    | NUMBER
    | CHAR;

series : (exp',')* exp;

func returns [int length]
    : 'sin'  {$length = 1}  // sin(angle)
    | 'cos'  {$length = 1}  // cos(angle)
    | 'log'  {$length = 2}  // log(base, n)
    | 'root' {$length = 2} // root(base, n)
    ;

non_lit_func [int priority]
    : {$priority == 1}? (SUM | SUB)
    | {$priority == 2}? (DIV | MULT)
    | {$priority == 3}? POW
    ;

NUMBER : DIGIT+;
DIGIT  : [0-9];
CHAR   : [a-zA-Z];
OTHER_SYMBOL : '!';
SUM : '+';
SUB : '-';
MULT : '*';
DIV : '/';
POW : '^';
OPENING_BRACKET : '(';
CLOSING_BRACKET : ')';

WHITESPACE: [ \t\r\n]-> skip;


/*

Parsing flow example :

    5 + 4 * 2 ^ 5

    Trying to parse "functions", nothing matches.
    Trying to parse "exp ^ exp" :

    1. Finds '^'
    2. Checks whether left and right are expressions :

        5 + 4 * 2 and 5

        >>> 5 + 4 * 2

            Trying to parse "functions", nothing matches
            Trying to parse "exp ^ exp", nothing matches
            Trying to parse "exp * exp" :

            1. Finds '*'
            2. Checks whether left and right are expressions :

                5 + 4 and 2

                >>> 5 + 4

                    Trying to parse "functions", nothing matches
                    Trying to parse "exp ^ exp", nothing matches
                    Trying to parse "exp * exp", nothing matches
                    Trying to parse "exp + exp" :

                    1. Finds '+'
                    2. Checks whether left and right are expressions :

                    5 and 4

                    >>> 5

                        Trying to parse "functions", nothing matches
                        Trying to parse "exp ^ exp", nothing matches
                        Trying to parse "exp * exp", nothing matches
                        Trying to parse "exp + exp", nothing matches

                        Finds number. Result -> 5 is a number type expression

                    >>> 4

                        Same procedure. Result -> 4 is a number type expression

                    Result -> 5 + 4 is a valid addition type expression

                >>> 2

                    Same procedure as below, matches "number". Result -> number type expression

                Result -> 5 + 4 * 2 is a valid multiplication type expression

        >>> 5

            Same procedure as below, matches "number". Result -> number type expression

        Result -> 5 + 4 * 2 ^ 5 is a valid powered type expression

*/