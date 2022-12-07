package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.ConstructionElementId;

public interface DeleteConstructionElementUseCase {

    ConstructionElement execute(ConstructionElementId id);

    void all();

}
