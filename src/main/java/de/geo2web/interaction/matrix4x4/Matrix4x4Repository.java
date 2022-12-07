package de.geo2web.interaction.matrix4x4;

import java.util.List;
import java.util.Optional;

public interface Matrix4x4Repository {

    List<Matrix4x4> findAll();

    Optional<Matrix4x4> findById(Matrix4x4Id id);

    Matrix4x4 save(Matrix4x4 input);

    Optional<Matrix4x4> delete(Matrix4x4Id id);

    void deleteAll();

}
