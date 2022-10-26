package de.geo2web.arithmetic;

import de.geo2web.arithmetic.function.Function;
import de.geo2web.arithmetic.operator.Operator;

import java.util.Arrays;

public interface VariableOperand extends Operand {

    String getName();

    Variable getVariable();

    static boolean containsVariable(final Operand[] operands) {
        return Arrays.stream(operands).anyMatch(x -> x instanceof VariableOperand);
    }

    static Instruction convertOperator(final Operand[] operands, final Operator operator) {
        return new Instruction(operands, operator.getSymbol(), operator.getNumOperands(), operator.isLeftAssociative(), false, operator.getPrecedence());
    }

    static Instruction convertFunction(final Operand[] operands, final Function function) {
        return new Instruction(operands, function.getName(), function.getNumArguments(), false, true, Operator.PRECEDENCE_MAX);
    }
}
