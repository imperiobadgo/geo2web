package de.geo2web.construction.adapter.rest;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.adapter.shared.ConstructionElementIdMapper;
import de.geo2web.construction.application.ConstructionElementChanges;
import de.geo2web.shared.EvaluationResult;
import de.geo2web.shared.EvaluationResultMapper;
import de.geo2web.shared.ExpressionInputMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        ConstructionElementIdMapper.class,
        ExpressionInputMapper.class,
        EvaluationResultMapper.class
})
public interface ConstructionElementMapper {

    ConstructionElementReadModel toReadModel(ConstructionElement domain);

    ConstructionElementChanges toChanges(ConstructionElementCreateModel model);

    ConstructionElementChanges toChanges(ConstructionElementWriteModel model);

    EvaluationResultReadModel toReadModel(EvaluationResult domain);

}
