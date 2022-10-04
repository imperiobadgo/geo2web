package de.geo2web.arithmetic;

import de.geo2web.arithmetic.function.FunctionEvaluation;
import de.geo2web.arithmetic.operator.OperationEvaluation;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Arrays;

import static de.geo2web.arithmetic.operator.OperationEvaluation.*;

@Value
@RequiredArgsConstructor
public class Vector implements VectorOperand {

    Operand[] values;

    @Override
    public Operand deepClone() {
        return new Vector(SerializationUtils.clone(values));
    }

    @Override
    public String toReadableString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(ArithmeticSettings.Instance().Open_Vector);
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            builder.append(values[i].toReadableString());
            if (i > 0){
                builder.append(ArithmeticSettings.Instance().Argument_Separator);
            }
        }
        builder.append(ArithmeticSettings.Instance().Close_Vector);
        return builder.toString();
    }

    @Override
    public Vector getVector() {
        return this;
    }

    public static Number[] toNumberArray(float[] input) {
        Number[] result = new Number[input.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Number(input[i]);
        }
        return result;
    }

    public static Number[] toNumberArray(double[] input) {
        Number[] result = new Number[input.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Number(input[i]);
        }
        return result;
    }

    /**
     * Adds two vectors. The dimension of the result vector is the max of both input vectors. Remaining entries are not altered.
     *
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
                result[i] = handlePlus(aVector.values[i], bVector.values[i]);
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
     *
     * @param a input vector
     * @param b input number
     * @return result vector
     */
    public static Vector add(VectorOperand a, NumberOperand b) {
        Vector aVector = a.getVector();
        Number bNumber = b.getNumber();
        Operand[] result = new Operand[aVector.values.length];
        Arrays.setAll(result, i -> handlePlus(aVector.values[i], bNumber));
        return new Vector(result);
    }

    /**
     * Subtracts two vectors. The dimension of the result vector is the max of both input vectors. Remaining entries are not altered.
     *
     * @param a input vector
     * @param b input vector
     * @return result vector (dimension max of both input vectors)
     */
    public static Vector sub(VectorOperand a, VectorOperand b) {
        Vector aVector = a.getVector();
        Vector bVector = b.getVector();
        Operand[] result = new Operand[Math.max(aVector.values.length, bVector.values.length)];
        for (int i = 0; i < result.length; i++) {
            if (aVector.values.length > i && bVector.values.length > i) {
                result[i] = handleMinus(aVector.values[i], bVector.values[i]);
            } else if (aVector.values.length > i) {
                result[i] = aVector.values[i].deepClone();
            } else {
                result[i] = bVector.values[i].deepClone();
            }
        }
        return new Vector(result);
    }

    /**
     * Subtracts a vector with a number. The given input is subtracted by every entry in the dimension array.
     *
     * @param a input vector
     * @param b input number
     * @return result vector
     */
    public static Vector sub(VectorOperand a, NumberOperand b) {
        Vector aVector = a.getVector();
        Number bNumber = b.getNumber();
        Operand[] result = new Operand[aVector.values.length];
        Arrays.setAll(result, i -> handleMinus(aVector.values[i], bNumber));
        return new Vector(result);
    }

    /**
     * Calculated the dot product of two vectors. Both input vectors must have the same dimension and contain only numbers. This implementation uses compensated summation.
     *
     * @param a input vector
     * @param b input vector
     * @return result number
     */
    public static Number dot(VectorOperand a, VectorOperand b) {
        Vector aVector = a.getVector();
        Vector bVector = b.getVector();
        if (aVector.values.length != bVector.values.length) {
            throw new ArithmeticException("Vector dimensions not the same!");
        }
        float[] sumInput = new float[aVector.values.length];
        for (int i = 0; i < aVector.values.length; i++) {
            Operand multResult = handleMult(aVector.values[i], bVector.values[i]);
            if (!(multResult instanceof NumberOperand)) {
                throw new ArithmeticException("Vector does contain other content than numbers!");
            }
            sumInput[i] = ((NumberOperand) multResult).getNumber().getValue();
        }
        return new Number(CompensatedUtil.compensatedSummation(sumInput));
    }

    /**
     * Calculates result by raising the dot product of the vector to the power of the given number.
     * <a href="https://www.euclideanspace.com/maths/algebra/vectors/vecAlgebra/powers/index.htm">Maths - Powers of Vectors</a>
     *
     * @param left  input vector
     * @param right pow number. Gets rounded to integer and cannot be smaller than 2.
     * @return even power: number, not even power: vector
     */
    public static Operand pow(VectorOperand left, NumberOperand right) {
        Number countNumber = right.getNumber();
        int power = Math.round(countNumber.getValue());
        if (power < 2) {
            throw new ArithmeticException("Power cannot be smaller than 2!");
        }

        boolean even = power % 2 == 0;

        int count = power / 2;//integer division. example: 5/2 -> 2
        Operand result = new Number(1f);
        for (int i = 0; i < count; i++) {
            result = handleMult(dot(left, left), result);
        }
        if (even) {
            return result;
        } else {
            //for uneven powers the dot-results have to be multiplies by the vector
            return handleMult(left, result);
        }
    }

    /**
     * Multiplies a vector with a number. The given input is multiplied to every entry in the dimension array.
     *
     * @param a input vector
     * @param b input number
     * @return result vector
     */
    public static Vector mult(VectorOperand a, NumberOperand b) {
        Vector aVector = a.getVector();
        Number bNumber = b.getNumber();
        Operand[] result = new Operand[aVector.values.length];
        Arrays.setAll(result, i -> handleMult(aVector.values[i], bNumber));
        return new Vector(result);
    }

    /**
     * Calculates the cross-product of two three-dimensional vectors. Defined right-handed Cartesian coordinate system.
     *
     * @param a input vector
     * @param b input vector
     * @return result vector
     */
    public static Vector cross(VectorOperand a, VectorOperand b) {
        int num_dimensions = 3;
        if (a.getVector().getValues().length != num_dimensions || b.getVector().getValues().length != num_dimensions) {
            throw new ArithmeticException("Cross-product only supports three-dimensional vectors!");
        }
        Vector aVector = a.getVector();
        Vector bVector = b.getVector();
        Operand[] result = new Operand[num_dimensions];
        result[0] = handleMinus(handleMult(aVector.values[1], bVector.values[2]), handleMult(aVector.values[2], bVector.values[1]));
        result[1] = handleMinus(handleMult(aVector.values[2], bVector.values[0]), handleMult(aVector.values[0], bVector.values[2]));
        result[2] = handleMinus(handleMult(aVector.values[0], bVector.values[1]), handleMult(aVector.values[1], bVector.values[0]));
        return new Vector(result);
    }

    /**
     * Calculated the length of the vector.
     *
     * @param a input vector
     * @return result number
     */
    public static Operand length(VectorOperand a) {
        return FunctionEvaluation.handleSqrt(pow(a, new Number(2f)));
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
