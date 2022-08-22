package de.geo2web.arithmetics;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.SerializationUtils;

@Value
@RequiredArgsConstructor
public class Vector implements VectorValue {

    Number[] values;

    @Override
    public Expression evaluate() {
        return this;
    }

    @Override
    public Expression deepClone() {
        return new Vector(SerializationUtils.clone(values));
    }

    @Override
    public Vector getVector() {
        return this;
    }

    public static Number[] toNumberArray(float[] input){
        Number[] result = new Number[input.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Number(input[i]);
        }
        return result;
    }

    /**
     * Adds two vectors. The dimension of the result vector is the max of both input vectors. Remaining entries are not altered.
     * @param a input vector
     * @param b input vector
     * @return result vector (dimension max of both input vectors)
     */
    public static Vector add(VectorValue a, VectorValue b) {
        Vector aVector = a.getVector();
        Vector bVector = b.getVector();
        Number[] result = new Number[Math.max(aVector.values.length, bVector.values.length)];
        for (int i = 0; i < result.length; i++) {
            if (aVector.values.length > i && bVector.values.length > i) {
                result[i] = Number.add(aVector.values[i], bVector.values[i]);
            } else if (aVector.values.length > i) {
                result[i] = (Number) aVector.values[i].deepClone();
            } else {
                result[i] = (Number) bVector.values[i].deepClone();
            }
        }
        return new Vector(result);
    }

    /**
     * Adds a vector with a number. The given input is added to every entry in the dimension array.
     * @param a input vector
     * @param b input number
     * @return result vector
     */
    public static Vector add(VectorValue a, NumberValue b) {
        Vector aVector = a.getVector();
        Number bNumber = b.getNumber();
        Number[] result = new Number[aVector.values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Number.add(aVector.values[i], bNumber);
        }
        return new Vector(result);
    }

}
