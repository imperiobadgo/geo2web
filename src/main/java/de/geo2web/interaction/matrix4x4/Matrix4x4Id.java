package de.geo2web.interaction.matrix4x4;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class Matrix4x4Id {
    @NonNull
    UUID value;

    @Override
    public String toString() {
        return value.toString();
    }
}
