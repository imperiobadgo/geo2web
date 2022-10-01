package de.geo2web.construction.adapter.persistence;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.adapter.shared.ConstructionElementIdMapper;
import de.geo2web.shared.ExpressionInputMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        ConstructionElementIdMapper.class,
        ExpressionInputMapper.class
})
interface ConstructionElementJpaMapper {

    ConstructionElementJpa toEntity(ConstructionElement domain);

    ConstructionElement toDomain(ConstructionElementJpa entity);

}
