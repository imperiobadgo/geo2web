package de.geo2web.interaction.matrix4x4.adapter.shared;

import de.geo2web.interaction.matrix4x4.Matrix4x4Id;

import java.util.UUID;

public class Matrix4x4IdMapper {

    public static Matrix4x4Id fromUuid(final UUID id) {
        return new Matrix4x4Id(id);
    }

    public static UUID toUuid(final Matrix4x4Id id){
        return id.getValue();
    }

    private Matrix4x4IdMapper() {throw new AssertionError();}
}
