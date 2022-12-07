package de.geo2web.interaction.construction;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class ConstructionElementId {

    @NonNull
    UUID value;

    @Override
    public String toString() {
        return value.toString();
    }
}
