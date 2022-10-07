package de.geo2web.construction;

import de.geo2web.arithmetic.ExpressionBuilder;
import de.geo2web.arithmetic.Operand;
import de.geo2web.construction.application.ReadConstructionElementUseCase;
import de.geo2web.shared.ElementName;
import de.geo2web.shared.ExpressionInput;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ConstructionElement {

    public static int InitialConstructionIndex = 0;

    @NonNull
    @Builder.Default
    ConstructionElementId id = new ConstructionElementId(UUID.randomUUID());

    @Builder.Default
    int constructionIndex = InitialConstructionIndex;

    ElementName name;

    ExpressionInput input;

    public Operand evaluate(final ReadConstructionElementUseCase read){
        List<ConstructionElement> orderedPreviousElements = read.findAllInOrderUntil(this.constructionIndex);

        //Evaluate all previous elements and get the results
        Map<String, Operand> variables = new HashMap<>();
        for (ConstructionElement element: orderedPreviousElements) {
            variables.put(element.getName().getName(), element.evaluate(read));
        }

        return new ExpressionBuilder(input.getValue())
                .variables(variables.keySet())
                .build()
                .setVariables(variables)
                .evaluate();
    }


}
