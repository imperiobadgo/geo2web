package de.geo2web.parser.operator;

import de.geo2web.parser.*;
import de.geo2web.parser.Number;

public class OperationEvaluation {

    public static Operand handlePlus(Operand left, Operand right) {
        if (left instanceof NumberOperand && right instanceof NumberOperand) {
            return Number.add((NumberOperand) left, (NumberOperand) right);
        } else if (left instanceof VectorOperand && right instanceof VectorOperand) {
            return Vector.add((VectorOperand) left, (VectorOperand) right);
        } else if (left instanceof VectorOperand && right instanceof NumberOperand) {
            return Vector.add((VectorOperand) left, (NumberOperand) right);
        } else if (left instanceof NumberOperand && right instanceof VectorOperand) {
            return Vector.add((VectorOperand) right, (NumberOperand) left);
        }else {
            throw new IllegalArgumentException("Operand combination not supported!");
//            Logger.log(Level.Debug, de.geo2web.arithmetic.OperationEvaluation.class, "handlePlus",
//                    "returned EmptyExpression with: " + left.toString() + " and " + right.toString());
//            return new EmptyExpression();
        }
    }

    public static Operand handleMinus(Operand left, Operand right) {
        if (left instanceof NumberOperand && right instanceof NumberOperand) {
            return Number.sub((NumberOperand) left, (NumberOperand) right);
        }
        throw new IllegalArgumentException("Operand combination not supported!");
    }

    public static Operand handleUnaryMinus(Operand left){
        if (left instanceof NumberOperand){
            return Number.unaryMinus((NumberOperand) left);
        } else if (left instanceof VectorOperand) {
            return Vector.unaryMinus((VectorOperand) left);
        }
        throw new IllegalArgumentException("Operand not supported!");
    }

    public static Operand handleUnaryPlus(Operand left){
        if (left instanceof NumberOperand){
            return Number.unaryPlus((NumberOperand) left);
        } else if (left instanceof VectorOperand) {
            return Vector.unaryPlus((VectorOperand) left);
        }
        throw new IllegalArgumentException("Operand not supported!");
    }

    public static Operand handleMult(Operand left, Operand right) {
        if (left instanceof NumberOperand && right instanceof NumberOperand) {
            return Number.mult((NumberOperand) left, (NumberOperand) right);
        }
        throw new IllegalArgumentException("Operand combination not supported!");
    }

    public static Operand handleDiv(Operand left, Operand right) {
        if (left instanceof NumberOperand && right instanceof NumberOperand) {
            return Number.div((NumberOperand) left, (NumberOperand) right);
        }
        throw new IllegalArgumentException("Operand combination not supported!");
    }

    public static Operand handlePow(Operand left, Operand right) {
        if (left instanceof NumberOperand && right instanceof NumberOperand) {
            return Number.pow((NumberOperand) left, (NumberOperand) right);
        }
        throw new IllegalArgumentException("Operand combination not supported!");
    }

    public static Operand handleMod(Operand left, Operand right) {
        if (left instanceof NumberOperand && right instanceof NumberOperand) {
            return Number.mod((NumberOperand) left, (NumberOperand) right);
        }
        throw new IllegalArgumentException("Operand combination not supported!");
    }

}