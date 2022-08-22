package de.geo2web.arithmetics;

public class NumberExpression extends ExpressionDecorator {

    private final Number value;

    public NumberExpression(Expression expressionToBeDecorated, final Number value) {
        super(expressionToBeDecorated);
        this.value = value;
    }

    @Override
    public Expression evaluate() {
        return super.evaluate();
    }

    @Override
    public Expression deepClone() {
        return new NumberExpression(expressionToBeDecorated, (Number) value.deepClone());
    }
}
