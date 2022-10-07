package de.geo2web.construction.application;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.ConstructionRepository;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateConstructionElementUseCaseImpl implements CreateConstructionElementUseCase {

    private final ConstructionRepository repository;

    private final ReadConstructionElementUseCase read;

    @Override
    public ConstructionElement execute(final ConstructionElementChanges input) {
        int constructionIndex = read.getNextConstructionIndex();
        final ConstructionElement element = input.apply(ConstructionElement.builder(), constructionIndex);
        Logger.log(Level.Info, CreateConstructionElementUseCaseImpl.class,
                "Creating at ConstructionIndex " + constructionIndex + ": " + element.toString());
        return repository.save(element);
    }
}
