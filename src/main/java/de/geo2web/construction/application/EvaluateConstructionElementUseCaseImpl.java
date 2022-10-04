package de.geo2web.construction.application;

import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.ConstructionElementId;
import de.geo2web.shared.EvaluationResult;
import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EvaluateConstructionElementUseCaseImpl implements EvaluateConstructionElementUseCase {

    private final ReadConstructionElementUseCase read;

    @Override
    public EvaluationResult execute(final ConstructionElementId id) {
        final ConstructionElement element = read.findById(id);
        EvaluationResult result = EvaluationResult.of(element.evaluate(read).toReadableString());

        Logger.log(Level.Info, EvaluateConstructionElementUseCaseImpl.class,
                "Evaluating " + element.getInput().getValue() + ": " + result.getResult());
        return result;
    }
}
