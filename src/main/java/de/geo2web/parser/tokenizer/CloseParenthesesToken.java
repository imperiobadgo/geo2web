package de.geo2web.parser.tokenizer;

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
