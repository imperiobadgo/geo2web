package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.shared.ElementName;
import de.geo2web.shared.ExpressionInput;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ConstructionElementChanges {

    ElementName name;

    ExpressionInput input;

    Matrix4x4 transform;

    public ConstructionElement apply(final ConstructionElement.ConstructionElementBuilder builder, final int constructionIndex){
        return builder
                .name(name)
                .input(input)
                .transform(transform)
                .constructionIndex(constructionIndex)
                .build();
    }

}
