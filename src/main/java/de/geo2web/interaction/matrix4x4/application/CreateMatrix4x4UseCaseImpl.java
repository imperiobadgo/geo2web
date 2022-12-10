package de.geo2web.interaction.matrix4x4.application;

import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.interaction.matrix4x4.Matrix4x4Repository;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMatrix4x4UseCaseImpl implements CreateMatrix4x4UseCase{

    private final Matrix4x4Repository repository;

    @Override
    public Matrix4x4 execute(Matrix4x4Changes input) {
        final Matrix4x4 matrix = input.apply(Matrix4x4.builder());

        Logger.log(Level.Info, CreateMatrix4x4UseCaseImpl.class,
                "Creating Matrix4x4: " + matrix);

        return repository.save(matrix);
    }
}
