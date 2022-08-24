package de.geo2web.arithmetic;

public class EmptyExpression extends ExpressionDecorator{

    public EmptyExpression() {
        super(null);
    }

    @Override
    public Expression evaluate() {
        return this;
    }

    @Override
    public Expression deepClone() {
        return new EmptyExpression();
    }
}