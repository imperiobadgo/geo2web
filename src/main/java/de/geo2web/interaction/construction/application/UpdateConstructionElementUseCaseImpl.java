package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.ConstructionElementId;
import de.geo2web.interaction.construction.ConstructionRepository;
import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.interaction.matrix4x4.application.Matrix4x4Changes;
import de.geo2web.interaction.matrix4x4.application.UpdateMatrix4x4UseCase;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateConstructionElementUseCaseImpl implements UpdateConstructionElementUseCase{

    private final ConstructionRepository repository;

    private final ReadConstructionElementUseCase read;

    private final UpdateMatrix4x4UseCase updateMatrix;

    @Override
    public ConstructionElement execute(final ConstructionElementId id, final ConstructionElementChanges input) {
        Logger.log(Level.Info, UpdateConstructionElementUseCaseImpl.class, "updating " + id.toString() + " to " + input.toString());
        final ConstructionElement oldElement = read.findById(id);
        final Matrix4x4 oldMatrix = oldElement.getTransform();

        Matrix4x4Changes matrixInput = Matrix4x4Changes.getMatrix4x4Changes(input.getTransform());
        final Matrix4x4 savedMatrix = updateMatrix.execute(oldMatrix.getId(), matrixInput);

        //Set saved matrix because id from ConstructionElementChanges input is wrong
        final ConstructionElementChanges.ConstructionElementChangesBuilder elementChangesBuilder = input.toBuilder();
        elementChangesBuilder.transform(savedMatrix);

        final ConstructionElement newElement = elementChangesBuilder.build()
                .apply(oldElement.toBuilder(), oldElement.getConstructionIndex());

        newElement.evaluate(read);
        return repository.save(newElement);
    }
}
