package de.geo2web.interaction.matrix4x4;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Matrix4x4 {

    @NonNull
    @Builder.Default
    Matrix4x4Id id = new Matrix4x4Id(UUID.randomUUID());

    int version;

    float n11;
    float n12;
    float n13;
    float n14;

    float n21;
    float n22;
    float n23;
    float n24;

    float n31;
    float n32;
    float n33;
    float n34;

    float n41;
    float n42;
    float n43;
    float n44;

    public static Matrix4x4 fromFloatArray(final float[] values) {

        if (values != null) {
            if (!(values.length == 0 || values.length == 16)) {
                throw new IllegalArgumentException("Matrix4x4 needs 16 values!");
            }
        }
        final Matrix4x4Builder builder = Matrix4x4.builder();
        if (values == null || values.length == 0) {
            //create Identity matrix
            builder.n11(1);
            builder.n22(1);
            builder.n33(1);
            builder.n44(1);
            return builder.build();
        }
        builder.n11(values[0]);
        builder.n12(values[1]);
        builder.n13(values[2]);
        builder.n14(values[3]);

        builder.n21(values[4]);
        builder.n22(values[5]);
        builder.n23(values[6]);
        builder.n24(values[7]);

        builder.n31(values[8]);
        builder.n32(values[9]);
        builder.n33(values[10]);
        builder.n34(values[11]);

        builder.n41(values[12]);
        builder.n42(values[13]);
        builder.n43(values[14]);
        builder.n44(values[15]);

        return builder.build();
    }

    public float[] getValues() {
        return new float[]{
                n11, n12, n13, n14,
                n21, n22, n23, n24,
                n31, n32, n33, n34,
                n41, n42, n43, n44
        };
    }

}
