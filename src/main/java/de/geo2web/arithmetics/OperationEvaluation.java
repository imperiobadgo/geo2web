package de.geo2web.arithmetics;

import de.geo2web.util.logging.Level;
import de.geo2web.util.logging.Logger;

public class OperationEvaluation {
    public static Expression handlePlus(Expression left, Expression right) {
        if (left instanceof NumberValue && right instanceof NumberValue) {
            return Number.add((NumberValue) left, (NumberValue) right);
        } else if (left instanceof VectorValue && right instanceof VectorValue) {
            return Vector.add((VectorValue) left, (VectorValue) right);
        } else if (left instanceof VectorValue && right instanceof NumberValue) {
            return Vector.add((VectorValue) left, (NumberValue) right);
        } else if (left instanceof NumberValue && right instanceof VectorValue) {
            return Vector.add((VectorValue) right, (NumberValue) left);
        }else {
            Logger.log(Level.Debug, OperationEvaluation.class, "handlePlus",
                    "returned EmptyExpression with: " + left.toString() + " and " + right.toString());
            return new EmptyExpression();
        }
    }

    public static Expression handleMinus(Expression left, Expression right) {
        if (left instanceof NumberValue && right instanceof NumberValue) {
            return new Number(((NumberValue) left).getNumber().getValue()
                    - ((NumberValue) right).getNumber().getValue());
        }
        return new EmptyExpression();
    }

}
