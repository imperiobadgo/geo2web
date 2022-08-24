/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic.tokenizer;

import de.geo2web.arithmetic.function.Function;

public class FunctionToken extends Token {
    private final Function function;

    public FunctionToken(final Function function) {
        super(Token.TOKEN_FUNCTION);
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }
}
