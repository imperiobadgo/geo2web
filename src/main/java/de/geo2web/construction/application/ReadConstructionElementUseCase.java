package de.geo2web.construction.application;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.ConstructionElementId;

import java.util.List;

public interface ReadConstructionElementUseCase {

    List<ConstructionElement> findAllInOrder();

    ConstructionElement findById(ConstructionElementId id);

}
