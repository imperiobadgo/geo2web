package de.geo2web.shared;

import lombok.Value;

@Value
public class ElementParameters {

    public static ElementParameters of(final String[] value) {
        return new ElementParameters(value);
    }

    public static boolean isValid(final String value) {
        return value != null && !value.isEmpty() && !value.isBlank();
    }

    String[] parameters;

    private ElementParameters(final String[] value) {
        this.parameters = value;
    }

}
