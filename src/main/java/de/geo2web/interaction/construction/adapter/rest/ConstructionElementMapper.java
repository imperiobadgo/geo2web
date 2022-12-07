package de.geo2web.interaction.construction.adapter.rest;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.adapter.shared.ConstructionElementIdMapper;
import de.geo2web.interaction.construction.application.ConstructionElementChanges;
import de.geo2web.interaction.matrix4x4.adapter.shared.Matrix4x4Mapper;
import de.geo2web.shared.ElementNameMapper;
import de.geo2web.shared.EvaluationResult;
import de.geo2web.shared.EvaluationResultMapper;
import de.geo2web.shared.ExpressionInputMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        ConstructionElementIdMapper.class,
        ExpressionInputMapper.class,
        EvaluationResultMapper.class,
        ElementNameMapper.class,
        Matrix4x4Mapper.class
})
public interface ConstructionElementMapper {

    ConstructionElementReadModel toReadModel(ConstructionElement domain);

    ConstructionElementChanges toChanges(ConstructionElementCreateModel model);

    ConstructionElementChanges toChanges(ConstructionElementWriteModel model);

    EvaluationResultReadModel toReadModel(EvaluationResult domain);

}
