package de.geo2web.interaction.construction;

import de.geo2web.arithmetic.*;
import de.geo2web.arithmetic.function.Function;
import de.geo2web.arithmetic.function.OperandBasedFunction;
import de.geo2web.interaction.construction.application.ReadConstructionElementUseCase;
import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.shared.ElementName;
import de.geo2web.shared.ElementParameters;
import de.geo2web.shared.EvaluationResult;
import de.geo2web.shared.ExpressionInput;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.*;

@Value
@Builder(toBuilder = true)
public class ConstructionElement {

    public static int InitialConstructionIndex = 0;

    @NonNull
    @Builder.Default
    ConstructionElementId id = new ConstructionElementId(UUID.randomUUID());

    @Builder.Default
    int constructionIndex = InitialConstructionIndex;

    int version;

    @NonFinal
    ElementName name;

    @NonFinal
    ElementParameters parameters;

    ExpressionInput input;

    Matrix4x4 transform;

    @NonFinal
    EvaluationResult output;

    public Operand evaluate(final ReadConstructionElementUseCase read) {
        final Expression expression = createExpression(read, input.getValue());

        final Operand result = expression.evaluate();

        if (result instanceof VariableFunctionOperand) {
            final VariableFunction function = ((VariableFunctionOperand) result).getFunction();
            this.name = ElementName.of(function.getName());
            this.parameters = ElementParameters.of(function.getParameters());
        }

        this.output = EvaluationResult.of(result.toReadableString());
        return result;
    }

    /**
     * Creates the {@link Expression} by searching for other referenced construction elements.
     * @param read Construction element repository.
     * @param input Expression input.
     * @return Prepared {@link Expression} with set variables and functions.
     */
    private Expression createExpression(final ReadConstructionElementUseCase read, String input) {
        final List<ConstructionElement> orderedPreviousElements = read.findAllInOrderUntil(this.constructionIndex);

        //Evaluate all previous elements and get the results
        Map<String, Operand> variables = new HashMap<>();
        List<Function> functions = new ArrayList<>();
        for (ConstructionElement element : orderedPreviousElements) {
            final Operand result = element.evaluate(read);
            boolean addToVariable = true;

            if (result instanceof VariableFunctionOperand) {
                final VariableFunction variableFunction = ((VariableFunctionOperand) result).getFunction();
                if (variableFunction.hasParameters()) {
                    //Through the parameters, the operand is treated as a function and not as a variable.
                    addToVariable = false;
                    functions.add(OperandBasedFunction.create(variableFunction.getName(), variableFunction.getParameters(), variableFunction.getFunctionBody()));
                }
            }

            if (addToVariable) {
                variables.put(element.getName().getName(), result);
            }
        }

        return new ExpressionBuilder(input)
                .variables(variables.keySet())
                .functions(functions)
                .build()
                .setVariables(variables);
    }


}
