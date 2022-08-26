package de.geo2web.arithmetic;

import de.geo2web.arithmetic.operator.OperationEvaluation;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Arrays;

@Value
@RequiredArgsConstructor
public class Vector implements VectorOperand{

    Operand[] values;

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

    public static Number[] toNumberArray(double[] input){
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
        Operand[] result = new Operand[Math.max(aVector.values.length, bVector.values.length)];
        for (int i = 0; i < result.length; i++) {
            if (aVector.values.length > i && bVector.values.length > i) {
                result[i] = OperationEvaluation.handlePlus(aVector.values[i], bVector.values[i]);
            } else if (aVector.values.length > i) {
                result[i] = aVector.values[i].deepClone();
            } else {
                result[i] = bVector.values[i].deepClone();
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
        Operand[] result = new Operand[aVector.values.length];
        Arrays.setAll(result, i -> OperationEvaluation.handlePlus(aVector.values[i], bNumber));
        return new Vector(result);
    }

    public static Vector unaryMinus(VectorOperand left) {
        Operand[] values = left.getVector().getValues();
        Operand[] result = new Operand[values.length];
        Arrays.setAll(result, i -> OperationEvaluation.handleUnaryMinus(values[i]));
        return new Vector(result);
    }

    public static Vector unaryPlus(VectorOperand left) {
        Operand[] values = left.getVector().getValues();
        Operand[] result = new Operand[values.length];
        Arrays.setAll(result, i -> OperationEvaluation.handleUnaryPlus(values[i]));
        return new Vector(result);
    }

}
