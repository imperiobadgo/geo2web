package de.geo2web.construction.application;

import de.geo2web.construction.ConstructionElementId;
import de.geo2web.shared.EvaluationResult;

public interface EvaluateConstructionElementUseCase {

    EvaluationResult execute(ConstructionElementId id);

}
