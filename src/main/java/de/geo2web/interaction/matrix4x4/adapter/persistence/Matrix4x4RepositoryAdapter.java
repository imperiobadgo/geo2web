package de.geo2web.interaction.matrix4x4.adapter.persistence;

import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.interaction.matrix4x4.Matrix4x4Id;
import de.geo2web.interaction.matrix4x4.Matrix4x4Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Matrix4x4RepositoryAdapter implements Matrix4x4Repository {

    private final Matrix4x4JpaRepository repository;

    private final Matrix4x4JpaMapper mapper;

    @Override
    public List<Matrix4x4> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Matrix4x4> findById(Matrix4x4Id id) {
        return repository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Matrix4x4 save(Matrix4x4 input) {
        final Matrix4x4Jpa entity = mapper.toEntity(input);
        final Matrix4x4Jpa saved = repository.saveAndFlush(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Matrix4x4> delete(Matrix4x4Id id) {
        final Optional<Matrix4x4Jpa> entity = repository.findById(id.getValue());
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        repository.delete(entity.get());
        return Optional.ofNullable(mapper.toDomain(entity.get()));
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
