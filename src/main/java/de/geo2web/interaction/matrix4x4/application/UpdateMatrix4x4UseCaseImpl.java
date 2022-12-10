package de.geo2web.interaction.matrix4x4.application;

import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.interaction.matrix4x4.Matrix4x4Id;
import de.geo2web.interaction.matrix4x4.Matrix4x4Repository;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateMatrix4x4UseCaseImpl implements UpdateMatrix4x4UseCase{

    private final Matrix4x4Repository repository;

    private final ReadMatrix4x4UseCase read;

    @Override
    public Matrix4x4 execute(Matrix4x4Id id, Matrix4x4Changes input) {
        Logger.log(Level.Info, UpdateMatrix4x4UseCaseImpl.class, "updating " + id.toString() + " to " + input.toString());
        final Matrix4x4 oldMatrix = read.findById(id);
        final Matrix4x4 newMatrix = input.apply(oldMatrix.toBuilder());
        return repository.save(newMatrix);
    }
}
