package de.geo2web.arithmetics;

import lombok.Value;

@Value
public class ExpressionNode implements Expression {

    Expression left;
    Expression right;
    Operator operator;

    public ExpressionNode(Expression left, Expression right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public Expression evaluate() {
        return operator.handle(this);
    }

    @Override
    public Expression deepClone() {
        return new ExpressionNode(left.deepClone(), right.deepClone(), operator);
    }

}
