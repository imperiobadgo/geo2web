package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.ConstructionRepository;
import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.interaction.matrix4x4.application.CreateMatrix4x4UseCase;
import de.geo2web.interaction.matrix4x4.application.Matrix4x4Changes;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateConstructionElementUseCaseImpl implements CreateConstructionElementUseCase {

    private final ConstructionRepository repository;

    private final ReadConstructionElementUseCase read;

    private final CreateMatrix4x4UseCase createMatrix;

    @Override
    public ConstructionElement execute(final ConstructionElementChanges input) {
        int constructionIndex = read.getNextConstructionIndex();

        Matrix4x4Changes matrixInput = Matrix4x4Changes.getMatrix4x4Changes(input.getTransform());
        final Matrix4x4 savedMatrix = createMatrix.execute(matrixInput);

        //Set saved matrix because id have been changed
        final ConstructionElementChanges.ConstructionElementChangesBuilder elementChangesBuilder = input.toBuilder();
        elementChangesBuilder.transform(savedMatrix);

        final ConstructionElement element = elementChangesBuilder.build()
                .apply(ConstructionElement.builder(), constructionIndex);

        element.evaluate(read);

        Logger.log(Level.Info, CreateConstructionElementUseCaseImpl.class,
                "Creating at ConstructionIndex " + constructionIndex + ": " + element);
        return repository.save(element);
    }
}
