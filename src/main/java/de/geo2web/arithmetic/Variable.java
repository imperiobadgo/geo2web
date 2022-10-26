package de.geo2web.arithmetic;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Variable implements VariableOperand {

    String name;

    @Override
    public Operand deepClone() {
        return new Variable(name);
    }

    @Override
    public String toReadableString() {
        return name;
    }

    @Override
    public Variable getVariable() {
        return this;
    }
}
