package de.geo2web.interaction.matrix4x4.application;

import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.interaction.matrix4x4.Matrix4x4Id;
import de.geo2web.interaction.matrix4x4.Matrix4x4Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReadMatrix4x4UseCaseImpl implements ReadMatrix4x4UseCase{

    private final Matrix4x4Repository repository;

    @Override
    public Matrix4x4 findById(Matrix4x4Id id) {
        return repository.findById(id)
                .orElseThrow(() -> new Matrix4x4NotFoundException(id));
    }

    @Override
    public List<Matrix4x4> findAll() {
        return repository.findAll();
    }
}
