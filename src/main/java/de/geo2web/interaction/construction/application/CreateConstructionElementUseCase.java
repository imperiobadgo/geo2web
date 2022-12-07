package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;

public interface CreateConstructionElementUseCase {

    ConstructionElement execute(ConstructionElementChanges input);
}
