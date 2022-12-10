package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElement;
import de.geo2web.interaction.construction.ConstructionElementId;
import de.geo2web.interaction.construction.ConstructionRepository;
import de.geo2web.interaction.matrix4x4.Matrix4x4;
import de.geo2web.interaction.matrix4x4.application.DeleteMatrix4x4UseCase;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteConstructionElementUseCaseImpl implements DeleteConstructionElementUseCase {

    private final ConstructionRepository repository;

    private final DeleteMatrix4x4UseCase deleteMatrix;

    @Override
    public ConstructionElement execute(final ConstructionElementId id) {
        Logger.log(Level.Info, DeleteConstructionElementUseCaseImpl.class, "deleting " + id.toString());

        final ConstructionElement deletedElement = repository.delete(id)
                .orElseThrow(() -> new ConstructionElementNotFoundException(id));

        final Matrix4x4 transform = deletedElement.getTransform();
        deleteMatrix.execute(transform.getId());

        return deletedElement;
    }

    @Override
    public void all() {
        Logger.log(Level.Info, DeleteConstructionElementUseCaseImpl.class, "deleting all");

        final List<ConstructionElement> elements = repository.findAllInOrder();

        repository.deleteAll();

        //delete all used matrices after elements are removed
        for (ConstructionElement element: elements) {
            final Matrix4x4 transform = element.getTransform();
            deleteMatrix.execute(transform.getId());
        }
    }
}
