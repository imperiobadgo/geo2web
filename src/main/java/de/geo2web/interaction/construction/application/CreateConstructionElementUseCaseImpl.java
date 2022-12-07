package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.ConstructionRepository;
import de.geo2web.interaction.matrix4x4.Matrix4x4Repository;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateConstructionElementUseCaseImpl implements CreateConstructionElementUseCase {

    private final ConstructionRepository repository;

    private final Matrix4x4Repository matrix4x4Repository;

    private final ReadConstructionElementUseCase read;

    @Override
    public ConstructionElement execute(final ConstructionElementChanges input) {
        int constructionIndex = read.getNextConstructionIndex();
        final ConstructionElement element = input.apply(ConstructionElement.builder(), constructionIndex);
        element.evaluate(read);
        Logger.log(Level.Info, CreateConstructionElementUseCaseImpl.class,
                "Creating at ConstructionIndex " + constructionIndex + ": " + element);
        matrix4x4Repository.save(element.getTransform());
        return repository.save(element);
    }
}
