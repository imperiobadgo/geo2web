package de.geo2web.shared;

public class ExpressionInputMapper {

    public static ExpressionInput fromString(final String value) {
        return ExpressionInput.of(value);
    }

    public static String toString(final ExpressionInput input) {
        return input.getValue();
    }

    private ExpressionInputMapper() {
        throw new AssertionError();
    }

}
