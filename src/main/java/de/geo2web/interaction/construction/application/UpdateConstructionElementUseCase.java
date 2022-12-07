package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.ConstructionElementId;

public interface UpdateConstructionElementUseCase {

    ConstructionElement execute(ConstructionElementId id, ConstructionElementChanges input);

}
