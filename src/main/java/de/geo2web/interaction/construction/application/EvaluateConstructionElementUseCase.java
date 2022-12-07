package de.geo2web.interaction.construction.application;

import de.geo2web.interaction.construction.ConstructionElementId;
import de.geo2web.shared.EvaluationResult;

public interface EvaluateConstructionElementUseCase {

    EvaluationResult execute(ConstructionElementId id);

}
