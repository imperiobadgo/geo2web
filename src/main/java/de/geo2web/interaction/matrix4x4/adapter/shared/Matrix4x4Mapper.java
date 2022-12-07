package de.geo2web.interaction.matrix4x4.adapter.shared;

import de.geo2web.interaction.matrix4x4.Matrix4x4;

public class Matrix4x4Mapper {

    public static Matrix4x4 toMatrix4x4(final float[] array) {
        return Matrix4x4.fromFloatArray(array);
    }

    public static float[] fromMatrix4x4(final Matrix4x4 matrix){
        return matrix.getValues();
    }

    private Matrix4x4Mapper() {throw new AssertionError();}
}
