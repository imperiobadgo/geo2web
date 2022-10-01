package de.geo2web.shared;

import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;
import lombok.Value;

@Value
public class ExpressionInput {

    public static ExpressionInput of(final String value) {
        return new ExpressionInput(value);
    }

    public static boolean isValid(final String value){
        return value != null && !value.isEmpty() && !value.isBlank();
    }

    String value;

    private ExpressionInput(final String value){
        if (isValid(value)) {
            this.value = value.trim();
        } else {
            Logger.log(Level.Warning, ExpressionInput.class, "Invalid input: " + value);
            throw new IllegalArgumentException("Invalid input.");
        }
    }

    @Override
    public String toString() {
        return value;
    }

}
