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
public class DeleteConstructionElementUseCaseImpl implements DeleteConstructionElementUseCase {

    private final ConstructionRepository repository;


    @Override
    public ConstructionElement execute(ConstructionElementId id) {
//        final ConstructionElement element = repository.findById(id)
//                .orElseThrow(() -> new ConstructionElementNotFoundException(id.toString()));

        Logger.log(Level.Info, DeleteConstructionElementUseCaseImpl.class, "deleting " + id.toString());

        return repository.delete(id)
                .orElseThrow(() -> new ConstructionElementNotFoundException(id.toString()));
    }

    @Override
    public void all() {
        Logger.log(Level.Info, DeleteConstructionElementUseCaseImpl.class, "deleting all");
        repository.deleteAll();
    }
}
