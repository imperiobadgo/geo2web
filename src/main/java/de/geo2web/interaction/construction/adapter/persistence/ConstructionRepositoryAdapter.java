package de.geo2web.interaction.construction.adapter.persistence;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.ConstructionElementId;
import de.geo2web.interaction.construction.ConstructionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConstructionRepositoryAdapter implements ConstructionRepository {

    private final ConstructionJpaRepository repository;

    private final ConstructionElementJpaMapper mapper;

    @Override
    public List<ConstructionElement> findAllInOrder() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .sorted(Comparator.comparingInt(ConstructionElement::getConstructionIndex))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConstructionElement> getLast() {
        final List<ConstructionElement> list = findAllInOrder();
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(list.size() - 1));
    }

    @Override
    public Optional<ConstructionElement> findById(ConstructionElementId id) {
        return repository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public ConstructionElement save(ConstructionElement input) {
        final ConstructionElementJpa entity = mapper.toEntity(input);
        final ConstructionElementJpa saved = repository.saveAndFlush(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ConstructionElement> delete(ConstructionElementId id) {
        final Optional<ConstructionElementJpa> entity = repository.findById(id.getValue());
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
