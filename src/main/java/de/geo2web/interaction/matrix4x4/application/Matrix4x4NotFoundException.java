package de.geo2web.interaction.matrix4x4.application;

import de.geo2web.interaction.matrix4x4.Matrix4x4Id;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;

public class Matrix4x4NotFoundException extends RuntimeException {

    public Matrix4x4NotFoundException(Matrix4x4Id id) {
        super(id.toString());
        Logger.log(Level.Error, Matrix4x4NotFoundException.class, id.toString());
    }
}
