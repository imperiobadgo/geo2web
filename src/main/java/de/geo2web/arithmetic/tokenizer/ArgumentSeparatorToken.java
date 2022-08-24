/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic.tokenizer;

/**
 * Represents an argument separator in functions i.e: ','
 */
class ArgumentSeparatorToken extends Token {
    /**
     * Create a new instance
     */
    ArgumentSeparatorToken() {
        super(Token.TOKEN_SEPARATOR);
    }
}
