package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.ConstructionElementId;
import de.geo2web.interaction.construction.ConstructionRepository;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteConstructionElementUseCaseImpl implements DeleteConstructionElementUseCase {

    private final ConstructionRepository repository;


    @Override
    public ConstructionElement execute(final ConstructionElementId id) {
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
