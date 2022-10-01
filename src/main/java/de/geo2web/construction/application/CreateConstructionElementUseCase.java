package de.geo2web.construction.application;

import de.geo2web.construction.ConstructionElement;

public interface CreateConstructionElementUseCase {

    ConstructionElement execute(ConstructionElementChanges input);
}
