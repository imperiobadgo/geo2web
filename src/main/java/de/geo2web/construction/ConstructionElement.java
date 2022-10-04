package de.geo2web.construction;

import de.geo2web.arithmetic.Expression;
import de.geo2web.arithmetic.ExpressionBuilder;
import de.geo2web.arithmetic.Operand;
import de.geo2web.construction.application.ReadConstructionElementUseCase;
import de.geo2web.shared.ExpressionInput;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

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

    ExpressionInput input;

    @NonFinal
    Expression expression;

    public Operand evaluate(final ReadConstructionElementUseCase read){
        expression = new ExpressionBuilder(input.getValue())
                .build();
        return expression.evaluate();
    }


}
