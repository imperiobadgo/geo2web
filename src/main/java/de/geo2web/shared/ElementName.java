package de.geo2web.shared;

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
        if (value == null)
            this.name = "";
        else
            this.name = value.trim();
    }

    @Override
    public String toString() {
        return name;
    }
}
