package de.geo2web.shared;

import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.Value;

@Value
public class EvaluationResult {

    public static EvaluationResult of(final String value) {
        return new EvaluationResult(value);
    }

    public static boolean isValid(final String value) {
        return value != null && !value.isEmpty() && !value.isBlank();
    }

    String result;

    private EvaluationResult(final String value) {
        if (isValid(value)) {
            this.result = value.trim();
        } else {
            Logger.log(Level.Warning, EvaluationResult.class, "Invalid input: " + value);
            throw new IllegalArgumentException("Invalid input.");
        }
    }

    @Override
    public String toString() {
        return result;
    }

}
