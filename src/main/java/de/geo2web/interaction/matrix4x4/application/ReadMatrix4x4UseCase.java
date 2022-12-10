package de.geo2web.interaction.matrix4x4.application;

import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.interaction.matrix4x4.Matrix4x4Id;

import java.util.List;

public interface ReadMatrix4x4UseCase {

    Matrix4x4 findById(Matrix4x4Id id);

    List<Matrix4x4> findAll();
}
