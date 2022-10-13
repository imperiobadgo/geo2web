package de.geo2web.construction.application;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.ConstructionElementId;
import de.geo2web.construction.ConstructionRepository;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateConstructionElementUseCaseImpl implements UpdateConstructionElementUseCase{

    private final ConstructionRepository repository;

    private final ReadConstructionElementUseCase read;

    @Override
    public ConstructionElement execute(final ConstructionElementId id, final ConstructionElementChanges input) {
        Logger.log(Level.Info, UpdateConstructionElementUseCaseImpl.class, "updating " + id.toString() + " to " + input.toString());
        final ConstructionElement oldElement = read.findById(id);
        final ConstructionElement newElement = input.apply(oldElement.toBuilder(), oldElement.getConstructionIndex());
        newElement.evaluate(read);
        return repository.save(newElement);
    }
}
