package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.ConstructionElementId;
import de.geo2web.interaction.construction.ConstructionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new ConstructionElementNotFoundException(id));
    }

    @Override
    public int getNextConstructionIndex() {
        final Optional<ConstructionElement> lastElement = repository.getLast();

        int constructionIndex = ConstructionElement.InitialConstructionIndex;
        if (lastElement.isPresent()) {
            constructionIndex = lastElement.get().getConstructionIndex() + 1;
        }
        return constructionIndex;
    }

}
