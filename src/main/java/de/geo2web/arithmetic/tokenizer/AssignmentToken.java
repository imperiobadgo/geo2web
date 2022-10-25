package de.geo2web.arithmetic.tokenizer;

/**
 *  Represents an assignment to declare a function
 */
public class AssignmentToken extends Token{

    String name;

    String[] parameters;

    /**
     * Create a new instance
     */
    public AssignmentToken(final String name, final String[] parameters) {
        super(Token.TOKEN_ASSIGNMENT);
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public String[] getParameters() {
        return parameters;
    }
}
