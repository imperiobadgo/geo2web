package de.geo2web.interaction.construction.adapter.rest;

import de.geo2web.Application;
import de.geo2web.interaction.construction.adapter.shared.ConstructionElementIdMapper;
import de.geo2web.interaction.construction.application.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RestController
@CrossOrigin(origins = Application.CrossOriginUrl)
@RequestMapping("/api/construction")
public class ConstructionsController {

    @Autowired
    private ConstructionElementMapper mapper;

    @Autowired
    private CreateConstructionElementUseCase create;

    @Autowired
    private ReadConstructionElementUseCase read;

    @Autowired
    private UpdateConstructionElementUseCase update;

    @Autowired
    private EvaluateConstructionElementUseCase evaluate;

    @Autowired
    private DeleteConstructionElementUseCase delete;

    @Operation(summary = "Creates a new construction element.")
    @PostMapping()
    public ConstructionElementReadModel create(@RequestBody final ConstructionElementCreateModel input) {
        return mapper.toReadModel(create.execute(mapper.toChanges(input)));
    }

    @Operation(summary = "Gets all construction elements in raising order.")
    @GetMapping()
    public List<ConstructionElementReadModel> findAllInOrder() {
        return read.findAllInOrder()
                .stream()
                .map(mapper::toReadModel)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Gets the searched construction element.")
    @GetMapping("/{id}")
    public ConstructionElementReadModel findById(@PathVariable("id") final UUID id) {
        return mapper.toReadModel(read.findById(ConstructionElementIdMapper.fromUuid(id)));
    }

    @Operation(summary = "Updates the given construction element to the new values.")
    @PutMapping()
    public ConstructionElementReadModel update(@RequestBody final ConstructionElementWriteModel input) {
        return mapper.toReadModel(update.execute(ConstructionElementIdMapper.fromUuid(input.id), mapper.toChanges(input)));
    }

    @Operation(summary = "Evaluates the given construction element.")
    @GetMapping("/{id}/evaluate")
    public EvaluationResultReadModel evaluate(@PathVariable("id") final UUID id) {
        return mapper.toReadModel(evaluate.execute(ConstructionElementIdMapper.fromUuid(id)));
    }

    @Operation(summary = "Deletes the given construction element.")
    @DeleteMapping("/{id}")
    public ConstructionElementReadModel delete(@PathVariable("id") final UUID id) {
        return mapper.toReadModel(delete.execute(ConstructionElementIdMapper.fromUuid(id)));
    }

    @Operation(summary = "Deletes the whole construction.")
    @DeleteMapping()
    public void deleteAll() {
        delete.all();
    }

}
