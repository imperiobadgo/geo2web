package de.geo2web.arithmetic;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.SerializationUtils;

@Value
@RequiredArgsConstructor
public class Vector implements VectorOperand{

    NumberOperand[] values;

    @Override
    public Operand deepClone() {
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
    public static Vector add(VectorOperand a, VectorOperand b) {
        Vector aVector = a.getVector();
        Vector bVector = b.getVector();
        NumberOperand[] result = new NumberOperand[Math.max(aVector.values.length, bVector.values.length)];
        for (int i = 0; i < result.length; i++) {
            if (aVector.values.length > i && bVector.values.length > i) {
                result[i] = Number.add(aVector.values[i], bVector.values[i]);
            } else if (aVector.values.length > i) {
                result[i] = (NumberOperand) aVector.values[i].deepClone();
            } else {
                result[i] = (NumberOperand) bVector.values[i].deepClone();
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
    public static Vector add(VectorOperand a, NumberOperand b) {
        Vector aVector = a.getVector();
        Number bNumber = b.getNumber();
        NumberOperand[] result = new NumberOperand[aVector.values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Number.add(aVector.values[i], bNumber);
        }
        return new Vector(result);
    }

    public static Vector unaryMinus(VectorOperand left) {
        NumberOperand[] values = left.getVector().getValues();
        NumberOperand[] result = new NumberOperand[values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Number.unaryMinus(values[i].getNumber());
        }
        return new Vector(result);
    }

    public static Vector unaryPlus(VectorOperand left) {
        return (Vector) left.deepClone();
    }

}
