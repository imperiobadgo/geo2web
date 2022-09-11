package de.geo2web.arithmetic.function;

import de.geo2web.arithmetic.*;
import de.geo2web.arithmetic.Number;

public class FunctionEvaluation {

    public static Operand handleSin(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.sin((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleCos(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.cos((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleTan(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.tan((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleCot(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.cot((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleLog(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.log((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleLog2(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.log2((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleLog10(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.log10((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleLog1p(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.log1p((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleAbs(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.abs((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleAcos(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.acos((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleAsin(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.asin((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleAtan(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.atan((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleCbrt(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.cbrt((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleFloor(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.floor((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleSinh(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.sinh((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleSqrt(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.sqrt((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleTanh(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.tanh((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleCosh(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.cosh((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleCeil(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.ceil((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handlePow(Operand a, Operand b) {
        if (a instanceof NumberOperand && b instanceof NumberOperand) {
            return Number.pow((NumberOperand) a, (NumberOperand) b);
        } else {
            throw new IllegalArgumentException("Operand combination not supported!");
        }
    }

    public static Operand handleExp(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.exp((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleExpm1(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.expm1((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleSgn(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.sgn((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleCsc(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.csc((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleSec(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.sec((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleCsch(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.csch((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleSech(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.sech((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleCoth(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.coth((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleLogb(Operand a, Operand b) {
        if (a instanceof NumberOperand && b instanceof NumberOperand) {
            return Number.logb((NumberOperand) a, (NumberOperand) b);
        } else {
            throw new IllegalArgumentException("Operand combination not supported!");
        }
    }

    public static Operand handleToRadians(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.toRadians((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleToDegrees(Operand a) {
        if (a instanceof NumberOperand) {
            return Number.toDegrees((NumberOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }

    public static Operand handleCross(Operand a, Operand b) {
        if (a instanceof VectorOperand && b instanceof VectorOperand) {
            return Vector.cross((VectorOperand) a, (VectorOperand) b);
        } else {
            throw new IllegalArgumentException("Operand combination not supported!");
        }
    }

    public static Operand handleLength(Operand a) {
        if (a instanceof VectorOperand) {
            return Vector.length((VectorOperand) a);
        } else {
            throw new IllegalArgumentException("Operand not supported!");
        }
    }
}
