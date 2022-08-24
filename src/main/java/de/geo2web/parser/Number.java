package de.geo2web.parser;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Number implements NumberOperand {

    float value;

    public Number(String input) {
        this.value = Float.parseFloat(input);
    }

    public Number(double d) {
        this.value = (float) d;
    }

    @Override
    public Number getNumber() {
        return this;
    }

    @Override
    public Operand deepClone() {
        return new Number(value);
    }

    //Operations

    public static Number add(NumberOperand a, NumberOperand b) {
        return new Number(a.getNumber().getValue() + b.getNumber().getValue());
    }

    public static Number sub(NumberOperand a, NumberOperand b) {
        return new Number(a.getNumber().getValue() - b.getNumber().getValue());
    }

    public static Number unaryMinus(NumberOperand a) {
        return new Number(-a.getNumber().getValue());
    }

    public static Number unaryPlus(NumberOperand a) {
        return new Number(a.getNumber().getValue());
    }

    public static Number mult(NumberOperand a, NumberOperand b) {
        return new Number(a.getNumber().getValue() * b.getNumber().getValue());
    }

    public static Number div(NumberOperand a, NumberOperand b) {
        if (b.getNumber().getValue() == 0f) {
            throw new ArithmeticException("Division by zero!");
        }
        return new Number(a.getNumber().getValue() / b.getNumber().getValue());
    }

    public static Number mod(NumberOperand a, NumberOperand b) {
        if (b.getNumber().getValue() == 0f) {
            throw new ArithmeticException("Division by zero!");
        }
        return new Number(a.getNumber().getValue() % b.getNumber().getValue());
    }

    //Functions

    public static Number sin(NumberOperand a) {
        return new Number(Math.sin(a.getNumber().getValue()));
    }

    public static Number cos(NumberOperand a) {
        return new Number(Math.cos(a.getNumber().getValue()));
    }

    public static Number tan(NumberOperand a) {
        return new Number(Math.tan(a.getNumber().getValue()));
    }

    public static Number cot(NumberOperand a) {
        double tan = Math.tan(a.getNumber().getValue());
        if (tan == 0f) {
            throw new ArithmeticException("Division by zero!");
        }
        return new Number(1f / tan);
    }

    public static Number log(NumberOperand a) {
        return new Number(Math.log(a.getNumber().getValue()));
    }

    public static Number log2(NumberOperand a) {
        return new Number(Math.log(a.getNumber().getValue()) / Math.log(2d));
    }

    public static Number log10(NumberOperand a) {
        return new Number(Math.log10(a.getNumber().getValue()));
    }

    public static Number log1p(NumberOperand a) {
        return new Number(Math.log1p(a.getNumber().getValue()));
    }

    public static Number abs(NumberOperand a) {
        return new Number(Math.abs(a.getNumber().getValue()));
    }

    public static Number acos(NumberOperand a) {
        return new Number(Math.acos(a.getNumber().getValue()));
    }

    public static Number asin(NumberOperand a) {
        return new Number(Math.asin(a.getNumber().getValue()));
    }

    public static Number atan(NumberOperand a) {
        return new Number(Math.atan(a.getNumber().getValue()));
    }

    public static Number cbrt(NumberOperand a) {
        return new Number(Math.cbrt(a.getNumber().getValue()));
    }

    public static Number floor(NumberOperand a) {
        return new Number(Math.floor(a.getNumber().getValue()));
    }

    public static Number sinh(NumberOperand a) {
        return new Number(Math.sinh(a.getNumber().getValue()));
    }

    public static Number sqrt(NumberOperand a) {
        return new Number(Math.sqrt(a.getNumber().getValue()));
    }

    public static Number tanh(NumberOperand a) {
        return new Number(Math.tanh(a.getNumber().getValue()));
    }

    public static Number cosh(NumberOperand a) {
        return new Number(Math.cosh(a.getNumber().getValue()));
    }

    public static Number ceil(NumberOperand a) {
        return new Number(Math.ceil(a.getNumber().getValue()));
    }

    public static Number pow(NumberOperand a, NumberOperand b) {
        return new Number(Math.pow(a.getNumber().getValue(), b.getNumber().getValue()));
    }

    public static Number exp(NumberOperand a) {
        return new Number(Math.exp(a.getNumber().getValue()));
    }

    public static Number expm1(NumberOperand a) {
        return new Number(Math.expm1(a.getNumber().getValue()));
    }

    public static Number sgn(NumberOperand a) {
        float arg = a.getNumber().getValue();
        if (arg > 0) {
            return new Number(1);
        } else if (arg < 0) {
            return new Number(-1);
        } else {
            return new Number(0);
        }
    }

    public static Number csc(NumberOperand a) {
        double sin = Math.sin(a.getNumber().getValue());
        if (sin == 0d) {
            throw new ArithmeticException("Division by zero in cosecant!");
        }
        return new Number(1d / sin);
    }

    public static Number sec(NumberOperand a) {
        double cos = Math.cos(a.getNumber().getValue());
        if (cos == 0d) {
            throw new ArithmeticException("Division by zero in secant!");
        }
        return new Number(1d / cos);
    }

    public static Number csch(NumberOperand a) {
        float arg = a.getNumber().getValue();
        //this would throw an ArithmeticException later as sinh(0) = 0
        if (arg == 0f) {
            return new Number(0);
        }
        return new Number(1d / Math.sinh(arg));
    }

    public static Number sech(NumberOperand a) {
        return new Number(1d / Math.cosh(a.getNumber().getValue()));
    }

    public static Number coth(NumberOperand a) {
        float arg = a.getNumber().getValue();
        return new Number(Math.cosh(arg) / Math.sinh(arg));
    }

    public static Number logb(NumberOperand a, NumberOperand b) {
        return new Number(Math.log(b.getNumber().getValue()) / Math.log(a.getNumber().getValue()));
    }

    public static Number toRadians(NumberOperand a) {
        return new Number(Math.toRadians(a.getNumber().getValue()));
    }

    public static Number toDegrees(NumberOperand a) {
        return new Number(Math.toDegrees(a.getNumber().getValue()));
    }

}
