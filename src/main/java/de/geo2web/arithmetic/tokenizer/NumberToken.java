/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic.tokenizer;

import de.geo2web.arithmetic.Number;

/**
 * Represents a number in the expression
 */
public final class NumberToken extends Token {
    private final Number value;

    /**
     * Create a new instance
     *
     * @param value the value of the number
     */
    public NumberToken(double value) {
        super(TOKEN_NUMBER);
        this.value = new Number((float)value);
    }

    public NumberToken(Number value) {
        super(TOKEN_NUMBER);
        this.value = value;
    }

    public NumberToken(String input) {
        this(new Number(input));
    }

    NumberToken(final char[] expression, final int offset, final int len) {
        this(new Number(String.valueOf(expression, offset, len)));
    }

    /**
     * Get the value of the number
     *
     * @return the value
     */
    public Number getValue() {
        return value;
    }
}
