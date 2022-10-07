package de.geo2web.shared;

public class ElementNameMapper {

    public static ElementName fromString(final String value) {
        return ElementName.of(value);
    }

    public static String toString(final ElementName input) {
        return input.getName();
    }

    private ElementNameMapper() {
        throw new AssertionError();
    }
}
