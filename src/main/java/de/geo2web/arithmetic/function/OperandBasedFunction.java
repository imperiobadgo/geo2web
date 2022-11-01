package de.geo2web.arithmetic.function;

import de.geo2web.arithmetic.ExpressionBuilder;
import de.geo2web.arithmetic.Operand;

import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a function based of an operand.
 * In the {@link de.geo2web.arithmetic.function.OperandBasedFunction#apply(Operand...)} the given function body is reevaluated with the given input parameters.
 */
public class OperandBasedFunction extends Function {

    String[] parameters;

    Operand functionBody;

    public static OperandBasedFunction create(String name, String[] parameters, Operand functionBody) {
        return new OperandBasedFunction(name, parameters, functionBody, parameters.length);
    }

    private OperandBasedFunction(String name, String[] parameters, Operand functionBody, int numArguments) {
        super(name, numArguments);
        this.parameters = parameters;
        this.functionBody = functionBody;
    }

    @Override
    public Operand apply(Operand... args) {
        if (args.length != numArguments) {
            throw new IllegalArgumentException("Mismatch of arguments count.");
        }
        Map<String, Operand> variables = new HashMap<>();
        for (int i = 0, parametersLength = parameters.length; i < parametersLength; i++) {
            String name = parameters[i];
            Operand operand = args[i];
            variables.put(name, operand);
        }

        return new ExpressionBuilder(functionBody.toReadableString())
                .variables(variables.keySet())
                .build()
                .setVariables(variables)
                .evaluate();
    }
}
