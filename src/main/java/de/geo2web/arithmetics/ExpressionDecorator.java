package de.geo2web.arithmetics;

public abstract class ExpressionDecorator implements Expression{

    protected final Expression expressionToBeDecorated;

    public ExpressionDecorator(Expression expressionToBeDecorated) {
        this.expressionToBeDecorated = expressionToBeDecorated;
    }

    @Override
    public Expression evaluate(){
        return expressionToBeDecorated.evaluate();
    }

}