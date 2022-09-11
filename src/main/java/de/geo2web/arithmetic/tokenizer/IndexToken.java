package de.geo2web.arithmetic.tokenizer;

/**
 * represents a index description used in an expression
 */
public class IndexToken extends Token {

    private final int numArguments;

    /**
     * Create a new instance
     *
     * @param numArguments number of index rows
     */
    public IndexToken(final int numArguments) {
        super(TOKEN_INDEX);
        this.numArguments = numArguments;
    }

    /**
     * Get the number of arguments for this index description
     *
     * @return the number of arguments
     */
    public int getNumArguments() {
        return numArguments;
    }
}
