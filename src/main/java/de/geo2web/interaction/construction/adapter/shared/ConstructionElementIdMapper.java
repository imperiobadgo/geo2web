package de.geo2web.interaction.construction.adapter.shared;

import de.geo2web.interaction.construction.ConstructionElementId;

import java.util.UUID;

public class ConstructionElementIdMapper {

    public static ConstructionElementId fromUuid(final UUID id) {
        return new ConstructionElementId(id);
    }

    public static UUID toUuid(final ConstructionElementId id){
        return id.getValue();
    }

    private ConstructionElementIdMapper() {throw new AssertionError();}
}
