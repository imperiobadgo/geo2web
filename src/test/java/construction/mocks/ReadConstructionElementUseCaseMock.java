package construction.mocks;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.ConstructionElementId;
import de.geo2web.construction.application.ReadConstructionElementUseCase;

import java.util.ArrayList;
import java.util.List;

public class ReadConstructionElementUseCaseMock implements ReadConstructionElementUseCase {

    private final List<ConstructionElement> elements = new ArrayList<>();

    @Override
    public List<ConstructionElement> findAllInOrder() {
        return elements;
    }

    @Override
    public ConstructionElement findById(final ConstructionElementId id) {
        return elements.stream()
                .filter(element -> element.getId().getValue() == id.getValue())
                .findAny()
                .orElse(null);
    }

    @Override
    public int getNextConstructionIndex() {
        if (elements.isEmpty())
            return ConstructionElement.InitialConstructionIndex;

        final ConstructionElement lastElement = elements.get(elements.size() - 1);
        return lastElement.getConstructionIndex() + 1;
    }

    public void Add(ConstructionElement element) {
        elements.add(element);
    }
}
