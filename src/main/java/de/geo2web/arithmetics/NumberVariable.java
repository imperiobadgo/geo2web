package de.geo2web.arithmetics;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@RequiredArgsConstructor
public class NumberVariable implements NumberValue {

    String varName;
    @NonFinal
    Number value;

    @Override
    public Expression evaluate() {
        return this;
    }

    @Override
    public Expression deepClone() {
        return new NumberVariable(varName).setValue(value);
    }

    @Override
    public Number getNumber() {
        return value;
    }

    public NumberVariable setValue(Number v){
        this.value = v;
        return this;
    }
}
