package de.geo2web.construction.application;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.ConstructionElementId;

public interface DeleteConstructionElementUseCase {

    ConstructionElement execute(ConstructionElementId id);

    void all();

}
