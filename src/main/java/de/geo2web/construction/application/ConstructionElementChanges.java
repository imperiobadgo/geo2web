package de.geo2web.construction.application;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.shared.ExpressionInput;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ConstructionElementChanges {

    ExpressionInput input;

    public ConstructionElement apply(final ConstructionElement.ConstructionElementBuilder builder, final int constructionIndex){
        return builder
                .input(input)
                .constructionIndex(constructionIndex)
                .build();
    }

}
