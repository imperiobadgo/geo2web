package de.geo2web.shared;

public class EvaluationResultMapper {

    public static EvaluationResult fromString(final String value) {
        return EvaluationResult.of(value);
    }

    public static String toString(final EvaluationResult input) {
        return input.getResult();
    }

    private EvaluationResultMapper() {
        throw new AssertionError();
    }

}
