package de.geo2web.arithmetic.tokenizer;

public class VectorToken extends Token {

    private final int numArguments;

    /**
     * Create a new instance
     *
     * @param numArguments number of vector columns
     */
    public VectorToken(final int numArguments) {
        super(TOKEN_VECTOR);
        this.numArguments = numArguments;
    }


    /**
     * Get the number of arguments for this vector
     *
     * @return the number of arguments
     */
    public int getNumArguments() {
        return numArguments;
    }
}
