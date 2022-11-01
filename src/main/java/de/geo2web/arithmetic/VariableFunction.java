package de.geo2web.arithmetic;

import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Mapping as operand for example: f(x)=5*x+2.
 */
@Value
@RequiredArgsConstructor
public class VariableFunction implements VariableFunctionOperand {

    Operand functionBody;

    String name;

    String[] parameters;

    @Override
    public Operand deepClone() {
        return new VariableFunction(functionBody, name, parameters.clone());
    }

    @Override
    public String toReadableString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(ArithmeticSettings.Instance().Open_Parentheses);
        for (int i = 0, variablesLength = parameters.length; i < variablesLength; i++) {
            builder.append(parameters[i]);
            if (i < variablesLength - 1){
                builder.append(ArithmeticSettings.Instance().Argument_Separator);
            }
        }
        builder.append(ArithmeticSettings.Instance().Close_Parentheses);
        builder.append(ArithmeticSettings.Instance().Assignment);
        builder.append(functionBody.toReadableString());
        return builder.toString();
    }

    @Override
    public VariableFunction getFunction() {
        return this;
    }

    public boolean hasParameters(){
        return parameters.length > 0;
    }
}
