package de.geo2web.construction;

import de.geo2web.arithmetic.Expression;
import de.geo2web.shared.ExpressionInput;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ConstructionElement {

    public static int InitialConstructionIndex = -1;

    @NonNull
    @Builder.Default
    ConstructionElementId id = new ConstructionElementId(UUID.randomUUID());

    @Builder.Default
    int constructionIndex = InitialConstructionIndex;

    ExpressionInput input;

    Expression expression;

}
