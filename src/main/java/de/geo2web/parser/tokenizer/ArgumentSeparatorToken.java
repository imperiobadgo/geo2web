package de.geo2web.parser.tokenizer;

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
