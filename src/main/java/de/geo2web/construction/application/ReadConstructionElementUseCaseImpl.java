package de.geo2web.construction.application;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.ConstructionElementId;
import de.geo2web.construction.ConstructionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReadConstructionElementUseCaseImpl implements ReadConstructionElementUseCase {

    private final ConstructionRepository repository;


    @Override
    public List<ConstructionElement> findAllInOrder() {
        return repository.findAllInOrder();
    }

    @Override
    public ConstructionElement findById(ConstructionElementId id) {
        return repository.findById(id)
                .orElseThrow(() -> new ConstructionElementNotFoundException(id.toString()));
    }
}
