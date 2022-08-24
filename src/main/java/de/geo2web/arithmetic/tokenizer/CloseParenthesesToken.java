/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic.tokenizer;

/**
 * Represents closed parentheses
 */
class CloseParenthesesToken extends Token {

    /**
     * Create a new instance
     */
    CloseParenthesesToken() {
        super(Token.TOKEN_PARENTHESES_CLOSE);
    }
}
