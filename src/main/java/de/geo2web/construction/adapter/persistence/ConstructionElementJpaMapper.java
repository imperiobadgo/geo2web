package de.geo2web.construction.adapter.persistence;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.adapter.shared.ConstructionElementIdMapper;
import de.geo2web.shared.ElementNameMapper;
import de.geo2web.shared.EvaluationResultMapper;
import de.geo2web.shared.ExpressionInputMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        ConstructionElementIdMapper.class,
        ExpressionInputMapper.class,
        EvaluationResultMapper.class,
        ElementNameMapper.class
})
interface ConstructionElementJpaMapper {

    ConstructionElementJpa toEntity(ConstructionElement domain);

    ConstructionElement toDomain(ConstructionElementJpa entity);

}
