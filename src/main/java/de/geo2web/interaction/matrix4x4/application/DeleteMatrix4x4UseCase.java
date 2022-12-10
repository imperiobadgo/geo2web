package de.geo2web.interaction.matrix4x4.application;

import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.interaction.matrix4x4.Matrix4x4Id;

public interface DeleteMatrix4x4UseCase {

    Matrix4x4 execute(Matrix4x4Id id);

    void all();
}
