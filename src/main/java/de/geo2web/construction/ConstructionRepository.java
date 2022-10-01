package de.geo2web.construction;

import java.util.List;
import java.util.Optional;

public interface ConstructionRepository {

    List<ConstructionElement> findAllInOrder();

    Optional<ConstructionElement> getLast();

    Optional<ConstructionElement> findById(ConstructionElementId id);

    ConstructionElement save(ConstructionElement input);

    Optional<ConstructionElement> delete(ConstructionElementId id);

    void deleteAll();

}
