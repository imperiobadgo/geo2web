package de.geo2web.shared;

import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.Value;

@Value
public class ElementName {

    public static ElementName of(final String value) {
        return new ElementName(value);
    }

    public static boolean isValid(final String value) {
        return value != null && !value.isEmpty() && !value.isBlank();
    }

    String name;

    private ElementName(final String value) {
        if (isValid(value)) {
            this.name = value.trim();
        } else {
            Logger.log(Level.Warning, EvaluationResult.class, "Invalid input: " + value);
            throw new IllegalArgumentException("Invalid input.");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
