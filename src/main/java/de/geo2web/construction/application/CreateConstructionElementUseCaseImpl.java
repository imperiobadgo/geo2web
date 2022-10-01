package de.geo2web.construction.application;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.ConstructionRepository;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateConstructionElementUseCaseImpl implements CreateConstructionElementUseCase {

    private final ConstructionRepository repository;

    @Override
    public ConstructionElement execute(ConstructionElementChanges input) {
        final Optional<ConstructionElement> lastElement = repository.getLast();

        int constructionIndex = ConstructionElement.InitialConstructionIndex;
        if (lastElement.isPresent()) {
            constructionIndex = lastElement.get().getConstructionIndex() + 1;
        }

        final ConstructionElement element = input.apply(ConstructionElement.builder(), constructionIndex);
        Logger.log(Level.Info, CreateConstructionElementUseCaseImpl.class,
                "Creating at ConstructionIndex " + constructionIndex + ": " + element.toString());
        return repository.save(element);
    }
}
