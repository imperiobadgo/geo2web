/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic.tokenizer;

/**
 * Abstract class for tokens to tokenize expressions
 */
public abstract class Token {
    public static final short TOKEN_NUMBER = 1;
    public static final short TOKEN_OPERATOR = 2;
    public static final short TOKEN_FUNCTION = 3;
    public static final short TOKEN_PARENTHESES_OPEN = 4;
    public static final short TOKEN_PARENTHESES_CLOSE = 5;
    public static final short TOKEN_VARIABLE = 6;
    public static final short TOKEN_SEPARATOR = 7;
    public static final short TOKEN_VECTOR = 8;
    public static final short TOKEN_INDEX = 9;

    private final int type;

    Token(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
