package de.geo2web.arithmetic;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Number implements NumberValue {

    float value;

    @Override
    public Expression evaluate() {
        return this;
    }

    @Override
    public Expression deepClone() {
        return new Number(value);
    }

    @Override
    public Number getNumber() {
        return this;
    }

    public static Number add(NumberValue a, NumberValue b) {
        return new Number(a.getNumber().getValue() + b.getNumber().getValue());
    }

    public static Number sub(NumberValue a, NumberValue b) {
        return new Number(a.getNumber().getValue() - b.getNumber().getValue());
    }

}
