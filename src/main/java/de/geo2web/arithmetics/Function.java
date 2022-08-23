package de.geo2web.arithmetics;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Arrays;

@Value
@RequiredArgsConstructor
public class Function implements Expression{

    ExpressionNode node;

    NumberVariable[] variables;

    @Override
    public Expression evaluate() {
        return node.evaluate();
    }

    @Override
    public Expression deepClone() {
        NumberVariable[] copied = new NumberVariable[variables.length];
        Arrays.setAll(copied, i -> (NumberVariable) variables[i].deepClone());
        return new Function((ExpressionNode) node.deepClone(), copied);
    }
}
