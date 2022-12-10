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
public class DeleteMatrix4x4UseCaseImpl implements DeleteMatrix4x4UseCase{

    private final Matrix4x4Repository repository;

    @Override
    public Matrix4x4 execute(Matrix4x4Id id) {
        Logger.log(Level.Info, DeleteMatrix4x4UseCaseImpl.class, "deleting " + id.toString());

        return repository.delete(id)
                .orElseThrow(() -> new Matrix4x4NotFoundException(id));
    }

    @Override
    public void all() {
        repository.deleteAll();
    }
}
