/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic.operator;

import de.geo2web.arithmetic.Operand;

public abstract class Operators {
    private static final int INDEX_ADDITION = 0;
    private static final int INDEX_SUBTRACTION = 1;
    private static final int INDEX_MULTIPLICATION = 2;
    private static final int INDEX_DIVISION = 3;
    private static final int INDEX_POWER = 4;
    private static final int INDEX_MODULO = 5;
    private static final int INDEX_UNARY_MINUS = 6;
    private static final int INDEX_UNARY_PLUS = 7;

    private static final Operator[] BUILT_IN_OPERATORS = new Operator[8];

    static {
        BUILT_IN_OPERATORS[INDEX_ADDITION] = new Operator("+", 2, true, Operator.PRECEDENCE_ADDITION) {
            @Override
            public Operand apply(final Operand... args) {
                return OperationEvaluation.handlePlus(args[0], args[1]);//args[0] + args[1];
            }
        };
        BUILT_IN_OPERATORS[INDEX_SUBTRACTION] = new Operator("-", 2, true, Operator.PRECEDENCE_ADDITION) {
            @Override
            public Operand apply(final Operand... args) {
                return OperationEvaluation.handleMinus(args[0], args[1]);//args[0] - args[1];
            }
        };
        BUILT_IN_OPERATORS[INDEX_UNARY_MINUS] = new Operator("-", 1, false, Operator.PRECEDENCE_UNARY_MINUS) {
            @Override
            public Operand apply(final Operand... args) {
                return OperationEvaluation.handleUnaryMinus(args[0]);// -args[0];
            }
        };
        BUILT_IN_OPERATORS[INDEX_UNARY_PLUS] = new Operator("+", 1, false, Operator.PRECEDENCE_UNARY_PLUS) {
            @Override
            public Operand apply(final Operand... args) {
                return OperationEvaluation.handleUnaryPlus(args[0]);// args[0];
            }
        };
        BUILT_IN_OPERATORS[INDEX_MULTIPLICATION] = new Operator("*", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
            @Override
            public Operand apply(final Operand... args) {

                return OperationEvaluation.handleMult(args[0], args[1]);// args[0] * args[1];
            }
        };
        BUILT_IN_OPERATORS[INDEX_DIVISION] = new Operator("/", 2, true, Operator.PRECEDENCE_DIVISION) {
            @Override
            public Operand apply(final Operand... args) {
                return OperationEvaluation.handleDiv(args[0], args[1]);
//                if (args[1] == 0d) {
//                    throw new ArithmeticException("Division by zero!");
//                }
//                return args[0] / args[1];
            }
        };
        BUILT_IN_OPERATORS[INDEX_POWER] = new Operator("^", 2, false, Operator.PRECEDENCE_POWER) {
            @Override
            public Operand apply(final Operand... args) {
                return OperationEvaluation.handlePow(args[0], args[1]);// Math.pow(args[0], args[1]);
            }
        };
        BUILT_IN_OPERATORS[INDEX_MODULO] = new Operator("%", 2, true, Operator.PRECEDENCE_MODULO) {
            @Override
            public Operand apply(final Operand... args) {
                return OperationEvaluation.handleMod(args[0], args[1]);
//                if (args[1] == 0d) {
//                    throw new ArithmeticException("Division by zero!");
//                }
//                return args[0] % args[1];
            }
        };
    }

    public static Operator getBuiltinOperator(final char symbol, final int numArguments) {
        switch (symbol) {
            case '+':
                if (numArguments != 1) {
                    return BUILT_IN_OPERATORS[INDEX_ADDITION];
                }

                return BUILT_IN_OPERATORS[INDEX_UNARY_PLUS];
            case '-':
                if (numArguments != 1) {
                    return BUILT_IN_OPERATORS[INDEX_SUBTRACTION];
                }

                return BUILT_IN_OPERATORS[INDEX_UNARY_MINUS];
            case '*':
                return BUILT_IN_OPERATORS[INDEX_MULTIPLICATION];
            case '÷':
            case '/':
                return BUILT_IN_OPERATORS[INDEX_DIVISION];
            case '^':
                return BUILT_IN_OPERATORS[INDEX_POWER];
            case '%':
                return BUILT_IN_OPERATORS[INDEX_MODULO];
            default:
                return null;
        }
    }

}
