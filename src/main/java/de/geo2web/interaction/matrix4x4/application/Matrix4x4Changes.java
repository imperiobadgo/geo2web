package de.geo2web.interaction.matrix4x4.application;

import de.geo2web.interaction.matrix4x4.Matrix4x4;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Matrix4x4Changes {

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

    public Matrix4x4 apply(final Matrix4x4.Matrix4x4Builder builder){
        return builder
                .n11(n11)
                .n12(n12)
                .n13(n13)
                .n14(n14)
                .n21(n21)
                .n22(n22)
                .n23(n23)
                .n24(n24)
                .n31(n31)
                .n32(n32)
                .n33(n33)
                .n34(n34)
                .n41(n41)
                .n42(n42)
                .n43(n43)
                .n44(n44)
                .build();
    }

    public static Matrix4x4Changes getMatrix4x4Changes(Matrix4x4 matrix){
        final Matrix4x4Changes.Matrix4x4ChangesBuilder builder = Matrix4x4Changes.builder();
        return builder
                .n11(matrix.getN11())
                .n12(matrix.getN12())
                .n13(matrix.getN13())
                .n14(matrix.getN14())
                .n21(matrix.getN21())
                .n22(matrix.getN22())
                .n23(matrix.getN23())
                .n24(matrix.getN24())
                .n31(matrix.getN31())
                .n32(matrix.getN32())
                .n33(matrix.getN33())
                .n34(matrix.getN34())
                .n41(matrix.getN41())
                .n42(matrix.getN42())
                .n43(matrix.getN43())
                .n44(matrix.getN44())
                .build();
    }

}
