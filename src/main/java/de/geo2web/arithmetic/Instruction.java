package de.geo2web.arithmetic;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.SerializationUtils;

/**
 * A class representing an operand version of a function or operator. Stores the single operands and name of the function or operator.
 */
@Value
@RequiredArgsConstructor
public class Instruction implements VariableOperand {

    Operand[] operands;
    String name;
    int numOperands;
    boolean leftAssociative;

    /**
     * Indicates whether this operator takes arguments (given in parentheses) or operands.
     */
    boolean hasArguments;
    int precedence;

    @Override
    public Operand deepClone() {
        return new Instruction(SerializationUtils.clone(operands), name, numOperands, leftAssociative, hasArguments, precedence);
    }

    @Override
    public String toReadableString() {
        final StringBuilder builder = new StringBuilder();
        if (hasArguments) {
            //Function
            builder.append(name);
            builder.append(ArithmeticSettings.Instance().Open_Parentheses);
            for (int i = 0; i < operands.length; i++) {
                Operand o = operands[i];
                builder.append(o.toReadableString());
                if (i < operands.length - 1) {
                    builder.append(ArithmeticSettings.Instance().Argument_Separator);
                }
            }
            builder.append(ArithmeticSettings.Instance().Close_Parentheses);
        } else {
            //Operator
            if (numOperands > 1) {
                builder.append(operands[0].toReadableString());
                builder.append(name);
                builder.append(operands[1].toReadableString());
            } else {
                if (leftAssociative) {
                    builder.append(operands[0].toReadableString());
                    builder.append(name);
                } else {
                    builder.append(name);
                    builder.append(operands[0].toReadableString());
                }
            }
        }
        return builder.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Variable getVariable() {
        return new Variable(name);
    }
}
