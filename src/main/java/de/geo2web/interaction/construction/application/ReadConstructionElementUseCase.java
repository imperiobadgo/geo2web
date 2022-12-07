package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.ConstructionElementId;

import java.util.List;
import java.util.stream.Collectors;

public interface ReadConstructionElementUseCase {

    List<ConstructionElement> findAllInOrder();

    default List<ConstructionElement> findAllInOrderUntil(int constructionIndex)
    {
        return findAllInOrder()
                .stream()
                .filter(item -> item.getConstructionIndex() < constructionIndex)
                .collect(Collectors.toList());
    }

    ConstructionElement findById(ConstructionElementId id);


    int getNextConstructionIndex();

}
