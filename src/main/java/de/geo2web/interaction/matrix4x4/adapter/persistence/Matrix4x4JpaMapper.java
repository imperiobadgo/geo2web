package de.geo2web.interaction.matrix4x4.adapter.persistence;

import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.interaction.matrix4x4.adapter.shared.Matrix4x4IdMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        Matrix4x4IdMapper.class
})
public interface Matrix4x4JpaMapper {

    Matrix4x4Jpa toEntity(Matrix4x4 domain);

    Matrix4x4 toDomain(Matrix4x4Jpa entity);
}
