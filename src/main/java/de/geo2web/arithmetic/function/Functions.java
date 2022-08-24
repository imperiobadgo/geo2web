/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic.function;

import de.geo2web.arithmetic.Operand;

/**
 * Class representing the builtin functions available for use in expressions
 */
public class Functions {

    private static final int INDEX_SIN = 0;
    private static final int INDEX_COS = 1;
    private static final int INDEX_TAN = 2;
    private static final int INDEX_CSC = 3;
    private static final int INDEX_SEC = 4;
    private static final int INDEX_COT = 5;
    private static final int INDEX_SINH = 6;
    private static final int INDEX_COSH = 7;
    private static final int INDEX_TANH = 8;
    private static final int INDEX_CSCH = 9;
    private static final int INDEX_SECH = 10;
    private static final int INDEX_COTH = 11;
    private static final int INDEX_ASIN = 12;
    private static final int INDEX_ACOS = 13;
    private static final int INDEX_ATAN = 14;
    private static final int INDEX_SQRT = 15;
    private static final int INDEX_CBRT = 16;
    private static final int INDEX_ABS = 17;
    private static final int INDEX_CEIL = 18;
    private static final int INDEX_FLOOR = 19;
    private static final int INDEX_POW = 20;
    private static final int INDEX_EXP = 21;
    private static final int INDEX_EXPM1 = 22;
    private static final int INDEX_LOG10 = 23;
    private static final int INDEX_LOG2 = 24;
    private static final int INDEX_LOG = 25;
    private static final int INDEX_LOG1P = 26;
    private static final int INDEX_LOGB = 27;
    private static final int INDEX_SGN = 28;
    private static final int INDEX_TO_RADIAN = 29;
    private static final int INDEX_TO_DEGREE = 30;

    private static final Function[] BUILT_IN_FUNCTIONS = new Function[31];

    static {
        BUILT_IN_FUNCTIONS[INDEX_SIN] = new Function("sin") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleSin(args[0]);// Math.sin(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COS] = new Function("cos") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleCos(args[0]);// Math.cos(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TAN] = new Function("tan") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleTan(args[0]);// Math.tan(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COT] = new Function("cot") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleCot(args[0]);
//                double tan = Math.tan(args[0]);
//                if (tan == 0d) {
//                    throw new ArithmeticException("Division by zero in cotangent!");
//                }
//                return 1d / tan;
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG] = new Function("log") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleLog(args[0]);// Math.log(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG2] = new Function("log2") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleLog2(args[0]);// Math.log(args[0]) / Math.log(2d);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG10] = new Function("log10") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleLog10(args[0]);// Math.log10(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG1P] = new Function("log1p") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleLog1p(args[0]);// Math.log1p(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ABS] = new Function("abs") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleAbs(args[0]);// Math.abs(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ACOS] = new Function("acos") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleAcos(args[0]);// Math.acos(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ASIN] = new Function("asin") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleAsin(args[0]);// Math.asin(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ATAN] = new Function("atan") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleAtan(args[0]);// Math.atan(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CBRT] = new Function("cbrt") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleCbrt(args[0]);// Math.cbrt(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_FLOOR] = new Function("floor") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleFloor(args[0]);// Math.floor(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SINH] = new Function("sinh") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleSinh(args[0]);// Math.sinh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SQRT] = new Function("sqrt") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleSqrt(args[0]);// Math.sqrt(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TANH] = new Function("tanh") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleTanh(args[0]);// Math.tanh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COSH] = new Function("cosh") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleCosh(args[0]);// Math.cosh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CEIL] = new Function("ceil") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleCeil(args[0]);// Math.ceil(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_POW] = new Function("pow", 2) {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handlePow(args[0], args[1]);// Math.pow(args[0], args[1]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_EXP] = new Function("exp", 1) {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleExp(args[0]);// Math.exp(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_EXPM1] = new Function("expm1", 1) {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleExpm1(args[0]);// Math.expm1(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SGN] = new Function("signum", 1) {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleSgn(args[0]);
//                if (args[0] > 0) {
//                    return 1;
//                } else if (args[0] < 0) {
//                    return -1;
//                } else {
//                    return 0;
//                }
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CSC] = new Function("csc") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleCsc(args[0]);
//                double sin = Math.sin(args[0]);
//                if (sin == 0d) {
//                    throw new ArithmeticException("Division by zero in cosecant!");
//                }
//                return 1d / sin;
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SEC] = new Function("sec") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleSec(args[0]);
//                double cos = Math.cos(args[0]);
//                if (cos == 0d) {
//                    throw new ArithmeticException("Division by zero in secant!");
//                }
//                return 1d / cos;
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CSCH] = new Function("csch") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleCsch(args[0]);
//                //this would throw an ArithmeticException later as sinh(0) = 0
//                if (args[0] == 0d) {
//                    return 0;
//                }
//
//                return 1d / Math.sinh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SECH] = new Function("sech") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleSech(args[0]);// 1d / Math.cosh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COTH] = new Function("coth") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleCoth(args[0]);// Math.cosh(args[0]) / Math.sinh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOGB] = new Function("logb", 2) {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleLogb(args[0], args[1]);// Math.log(args[1]) / Math.log(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TO_RADIAN] = new Function("toradian") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleToRadians(args[0]);// Math.toRadians(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TO_DEGREE] = new Function("todegree") {
            @Override
            public Operand apply(Operand... args) {
                return FunctionEvaluation.handleToDegrees(args[0]);// Math.toDegrees(args[0]);
            }
        };

    }

    /**
     * Get the builtin function for a given name
     *
     * @param name te name of the function
     * @return a Function instance
     */
    public static Function getBuiltinFunction(final String name) {

        switch (name) {
            case "sin":
                return BUILT_IN_FUNCTIONS[INDEX_SIN];
            case "cos":
                return BUILT_IN_FUNCTIONS[INDEX_COS];
            case "tan":
                return BUILT_IN_FUNCTIONS[INDEX_TAN];
            case "cot":
                return BUILT_IN_FUNCTIONS[INDEX_COT];
            case "asin":
                return BUILT_IN_FUNCTIONS[INDEX_ASIN];
            case "acos":
                return BUILT_IN_FUNCTIONS[INDEX_ACOS];
            case "atan":
                return BUILT_IN_FUNCTIONS[INDEX_ATAN];
            case "sinh":
                return BUILT_IN_FUNCTIONS[INDEX_SINH];
            case "cosh":
                return BUILT_IN_FUNCTIONS[INDEX_COSH];
            case "tanh":
                return BUILT_IN_FUNCTIONS[INDEX_TANH];
            case "abs":
                return BUILT_IN_FUNCTIONS[INDEX_ABS];
            case "log":
                return BUILT_IN_FUNCTIONS[INDEX_LOG];
            case "log10":
                return BUILT_IN_FUNCTIONS[INDEX_LOG10];
            case "log2":
                return BUILT_IN_FUNCTIONS[INDEX_LOG2];
            case "log1p":
                return BUILT_IN_FUNCTIONS[INDEX_LOG1P];
            case "ceil":
                return BUILT_IN_FUNCTIONS[INDEX_CEIL];
            case "floor":
                return BUILT_IN_FUNCTIONS[INDEX_FLOOR];
            case "sqrt":
                return BUILT_IN_FUNCTIONS[INDEX_SQRT];
            case "cbrt":
                return BUILT_IN_FUNCTIONS[INDEX_CBRT];
            case "pow":
                return BUILT_IN_FUNCTIONS[INDEX_POW];
            case "exp":
                return BUILT_IN_FUNCTIONS[INDEX_EXP];
            case "expm1":
                return BUILT_IN_FUNCTIONS[INDEX_EXPM1];
            case "signum":
                return BUILT_IN_FUNCTIONS[INDEX_SGN];
            case "csc":
                return BUILT_IN_FUNCTIONS[INDEX_CSC];
            case "sec":
                return BUILT_IN_FUNCTIONS[INDEX_SEC];
            case "csch":
                return BUILT_IN_FUNCTIONS[INDEX_CSCH];
            case "sech":
                return BUILT_IN_FUNCTIONS[INDEX_SECH];
            case "coth":
                return BUILT_IN_FUNCTIONS[INDEX_COTH];
            case "toradian":
                return BUILT_IN_FUNCTIONS[INDEX_TO_RADIAN];
            case "todegree":
                return BUILT_IN_FUNCTIONS[INDEX_TO_DEGREE];
            default:
                return null;
        }
    }

}
