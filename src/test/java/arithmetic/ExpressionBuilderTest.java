/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package arithmetic;

import de.geo2web.arithmetic.function.Function;
import de.geo2web.arithmetic.operator.Operator;
import de.geo2web.arithmetic.*;
import de.geo2web.arithmetic.Number;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static arithmetic.TestUtil.*;
import static java.lang.Math.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ExpressionBuilderTest {

    @Test
    public void testExpressionBuilder1() {
        Operand result = new ExpressionBuilder("2+1")
                .build()
                .evaluate();
        assertEquals(3d, ((Number) result).getValue(), 0d);
    }

    @Test
    public void testExpressionBuilder2() {
        Operand result = new ExpressionBuilder("cos(x)")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.PI))
                .evaluate();
        double expected = cos(Math.PI);
        assertEquals(expected, ((Number) result).getValue(), 0d);
    }

    @Test
    public void testExpressionBuilder3() {
        double x = Math.PI;
        Operand result = new ExpressionBuilder("sin(x)-log(3*x/4)")
                .variables("x")
                .build()
                .setVariable("x", new Number(x))
                .evaluate();

        double expected = sin(x) - log(3 * x / 4);
        assertEquals(expected, ((Number) result).getValue(), EPSILON);
    }

    @Test
    public void testExpressionBuilder4() {
        Function log2 = new Function("log2", 1) {

            @Override
            public Operand apply(Operand... args) {
                return new Number(Math.log(((Number) args[0]).getValue()) / Math.log(2));
            }
        };
        Operand result = new ExpressionBuilder("log2(4)")
                .function(log2)
                .build()
                .evaluate();

        double expected = 2;
        assertEquals(expected, ((Number) result).getValue(), 0d);
    }

    @Test
    public void testExpressionBuilder5() {
        Function avg = new Function("avg", 4) {

            @Override
            public Operand apply(Operand... args) {
                double sum = 0;
                for (Operand arg : args) {
                    sum += ((Number) arg).getValue();
                }
                return new Number(sum / args.length);
            }
        };
        Operand result = new ExpressionBuilder("avg(1,2,3,4)")
                .function(avg)
                .build()
                .evaluate();

        double expected = 2.5d;
        assertEquals(expected, ((Number) result).getValue(), 0d);
    }

    @Test
    public void testExpressionBuilder6() {
        Operator factorial = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {

            @Override
            public Operand apply(Operand... args) {
                final Number arg = (Number) args[0];
                final int argInt = (int) arg.getValue();
                if ((double) argInt != arg.getValue()) {
                    throw new IllegalArgumentException("Operand for factorial has to be an integer");
                }
                if (argInt < 0) {
                    throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
                }
                float result = 1;
                for (int i = 1; i <= argInt; i++) {
                    result *= i;
                }
                return new Number(result);
            }
        };

        Operand result = new ExpressionBuilder("3!")
                .operator(factorial)
                .build()
                .evaluate();

        double expected = 6d;
        assertEquals(expected, ((Number) result).getValue(), 0d);
    }

    @Test
    public void testExpressionBuilder7() {
        ValidationResult res = new ExpressionBuilder("x")
                .variables("x")
                .build()
                .validate();
        assertFalse(res.isValid());
        assertEquals(res.getErrors().size(), 1);
    }

    @Test
    public void testExpressionBuilder8() {
        ValidationResult res = new ExpressionBuilder("x*y*z")
                .variables("x", "y", "z")
                .build()
                .validate();
        assertFalse(res.isValid());
        assertEquals(res.getErrors().size(), 3);
    }

    @Test
    public void testExpressionBuilder9() {
        ValidationResult res = new ExpressionBuilder("x")
                .variables("x")
                .build()
                .setVariable("x", new Number(1d))
                .validate();
        assertTrue(res.isValid());
    }

    @Test
    public void testValidationDocExample() {
        Expression e = new ExpressionBuilder("x")
                .variables("x")
                .build();
        ValidationResult res = e.validate();
        assertFalse(res.isValid());
        assertEquals(1, res.getErrors().size());

        e.setVariable("x", new Number(1d));
        res = e.validate();
        assertTrue(res.isValid());
    }

    @Test
    public void testExpressionBuilder10() {
        Operand result = new ExpressionBuilder("1e1")
                .build()
                .evaluate();
        assertEquals(10d, ((Number) result).getValue(), 0d);
    }

    @Test
    public void testExpressionBuilder11() {
        Operand result = new ExpressionBuilder("1.11e-1")
                .build()
                .evaluate();
        assertEquals(0.111f, ((Number) result).getValue(), 0d);
    }

    @Test
    public void testExpressionBuilder12() {
        Operand result = new ExpressionBuilder("1.11e+1")
                .build()
                .evaluate();
        assertEquals(11.1f, ((Number) result).getValue(), 0d);
    }

    @Test
    public void testExpressionBuilder13() {
        Operand result = new ExpressionBuilder("-3^2")
                .build()
                .evaluate();
        assertEquals(-9d, ((Number) result).getValue(), 0d);
    }

    @Test
    public void testExpressionBuilder14() {
        Operand result = new ExpressionBuilder("(-3)^2")
                .build()
                .evaluate();
        assertEquals(9d, ((Number) result).getValue(), 0d);
    }

    @Test(expected = ArithmeticException.class)
    public void testExpressionBuilder15() {
        Operand result = new ExpressionBuilder("-3/0")
                .build()
                .evaluate();
    }

    @Test
    public void testExpressionBuilder16() {
        Operand result = new ExpressionBuilder("log(x) - y * (sqrt(x^cos(y)))")
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(1d))
                .setVariable("y", new Number(2d))
                .evaluate();
    }

    @Test
    public void testExpressionBuilder17() {
        Expression e = new ExpressionBuilder("x-y*")
                .variables("x", "y")
                .build();
        ValidationResult res = e.validate(false);
        assertFalse(res.isValid());
        assertEquals(1, res.getErrors().size());
        assertEquals("Too many operators", res.getErrors().get(0));
    }

    @Test
    public void testExpressionBuilder18() {
        Expression e = new ExpressionBuilder("log(x) - y *")
                .variables("x", "y")
                .build();
        ValidationResult res = e.validate(false);
        assertFalse(res.isValid());
        assertEquals(1, res.getErrors().size());
        assertEquals("Too many operators", res.getErrors().get(0));
    }

    @Test
    public void testExpressionBuilder19() {
        Expression e = new ExpressionBuilder("x - y *")
                .variables("x", "y")
                .build();
        ValidationResult res = e.validate(false);
        assertFalse(res.isValid());
        assertEquals(1, res.getErrors().size());
        assertEquals("Too many operators", res.getErrors().get(0));
    }

    /* legacy tests from earlier exp4j versions */

    @Test
    public void testFunction1() {
        Function custom = new Function("timespi") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number) values[0]).getValue() * Math.PI);
            }
        };
        Expression e = new ExpressionBuilder("timespi(x)")
                .function(custom)
                .variables("x")
                .build()
                .setVariable("x", new Number(1));
        Operand result = e.evaluate();
        assertEquals(((Number) result).getValue(), PI, EPSILON);
    }

    @Test
    public void testFunction2() {
        Function custom = new Function("loglog") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(Math.log(Math.log(((Number) values[0]).getValue())));
            }
        };
        Expression e = new ExpressionBuilder("loglog(x)")
                .variables("x")
                .function(custom)
                .build()
                .setVariable("x", new Number(1));
        Operand result = e.evaluate();
        assertEquals(((Number) result).getValue(), log(log(1)), 0.0);
    }

    @Test
    public void testFunction3() {
        Function custom1 = new Function("foo") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number) values[0]).getValue() * Math.E);
            }
        };
        Function custom2 = new Function("bar") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number) values[0]).getValue() * Math.PI);
            }
        };
        Expression e = new ExpressionBuilder("foo(bar(x))")
                .function(custom1)
                .function(custom2)
                .variables("x")
                .build()
                .setVariable("x", new Number(1));
        Operand result = e.evaluate();
        assertEquals(((Number) result).getValue(), 1 * E * PI, EPSILON);
    }

    @Test
    public void testFunction4() {
        Function custom1 = new Function("foo") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number) values[0]).getValue() * Math.E);
            }
        };
        double varX = 32.24979131d;
        Expression e = new ExpressionBuilder("foo(log(x))")
                .variables("x")
                .function(custom1)
                .build()
                .setVariable("x", new Number(varX));
        Operand result = e.evaluate();
        assertEquals(((Number) result).getValue(), log(varX) * E, EPSILON);
    }

    @Test
    public void testFunction5() {
        Function custom1 = new Function("foo") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number) values[0]).getValue() * Math.E);
            }
        };
        Function custom2 = new Function("bar") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number) values[0]).getValue() * Math.PI);
            }
        };
        double varX = 32.24979131d;
        Expression e = new ExpressionBuilder("bar(foo(log(x)))")
                .variables("x")
                .function(custom1)
                .function(custom2)
                .build()
                .setVariable("x", new Number(varX));
        Operand result = e.evaluate();
        assertEquals(((Number) result).getValue(), log(varX) * E * PI, EPSILON);
    }

    @Test
    public void testFunction6() {
        Function custom1 = new Function("foo") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number) values[0]).getValue() * Math.E);
            }
        };
        Function custom2 = new Function("bar") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number) values[0]).getValue() * Math.PI);
            }
        };
        double varX = 32.24979131d;
        Expression e = new ExpressionBuilder("bar(foo(log(x)))")
                .variables("x")
                .functions(custom1, custom2)
                .build()
                .setVariable("x", new Number(varX));
        Operand result = e.evaluate();
        assertEquals(((Number) result).getValue(), log(varX) * E * PI, EPSILON);
    }

    @Test
    public void testFunction7() {
        Function custom1 = new Function("half") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number) values[0]).getValue() / 2);
            }
        };
        Expression e = new ExpressionBuilder("half(x)")
                .variables("x")
                .function(custom1)
                .build()
                .setVariable("x", new Number(1d));
        assertEquals(0.5d, ((Number) e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testFunction10() {
        Function custom1 = new Function("max", 2) {

            @Override
            public Operand apply(Operand... values) {
                return new Number(Math.max(((Number) values[0]).getValue(), ((Number) values[1]).getValue()));
            }
        };
        Expression e =
                new ExpressionBuilder("max(x,y)")
                        .variables("x", "y")
                        .function(custom1)
                        .build()
                        .setVariable("x", new Number(1d))
                        .setVariable("y", new Number(2d));
        assertEquals(2, ((Number) e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testFunction11() {
        Function custom1 = new Function("power", 2) {

            @Override
            public Operand apply(Operand... values) {
                return new Number(Math.pow(((Number) values[0]).getValue(), ((Number) values[1]).getValue()));
            }
        };
        Expression e =
                new ExpressionBuilder("power(x,y)")
                        .variables("x", "y")
                        .function(custom1)
                        .build()
                        .setVariable("x", new Number(2d))
                        .setVariable("y",
                                new Number(4d));
        assertEquals(pow(2, 4), ((Number) e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testFunction12() {
        Function custom1 = new Function("max", 5) {

            @Override
            public Operand apply(Operand... values) {
                double max = ((Number)values[0]).getValue();
                for (int i = 1; i < numArguments; i++) {
                    if (((Number)values[i]).getValue() > max) {
                        max = ((Number)values[i]).getValue();
                    }
                }
                return new Number(max);
            }
        };
        Expression e = new ExpressionBuilder("max(1,2.43311,51.13,43,12)")
                .function(custom1)
                .build();
        assertEquals(51.13f, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testFunction13() {
        Function custom1 = new Function("max", 3) {

            @Override
            public Operand apply(Operand... values) {
                double max = ((Number)values[0]).getValue();
                for (int i = 1; i < numArguments; i++) {
                    if (((Number)values[i]).getValue() > max) {
                        max = ((Number)values[i]).getValue();
                    }
                }
                return new Number(max);
            }
        };
        double varX = Math.E;
        Expression e = new ExpressionBuilder("max(log(x),sin(x),x)")
                .variables("x")
                .function(custom1)
                .build()
                .setVariable("x", new Number(varX));
        assertEquals(varX, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testFunction14() {
        Function custom1 = new Function("multiply", 2) {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number)values[0]).getValue() * ((Number)values[1]).getValue());
            }
        };
        double varX = 1;
        Expression e = new ExpressionBuilder("multiply(sin(x),x+1)")
                .variables("x")
                .function(custom1)
                .build()
                .setVariable("x", new Number(varX));
        double expected = Math.sin(varX) * (varX + 1);
        double actual = ((Number)e.evaluate()).getValue();
        assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testFunction15() {
        Function custom1 = new Function("timesPi") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number)values[0]).getValue() * Math.PI);
            }
        };
        double varX = 1;
        Expression e = new ExpressionBuilder("timesPi(x^2)")
                .variables("x")
                .function(custom1)
                .build()
                .setVariable("x", new Number(varX));
        double expected = varX * Math.PI;
        double actual = ((Number)e.evaluate()).getValue();
        assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testFunction16() {
        Function custom1 = new Function("multiply", 3) {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number)values[0]).getValue() * ((Number)values[1]).getValue() * ((Number)values[2]).getValue());
            }
        };
        double varX = 1;
        Expression e = new ExpressionBuilder("multiply(sin(x),x+1^(-2),log(x))")
                .variables("x")
                .function(custom1)
                .build()
                .setVariable("x", new Number(varX));
        double expected = Math.sin(varX) * Math.pow((varX + 1), -2) * Math.log(varX);
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testFunction17() {
        Function custom1 = new Function("timesPi") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number)values[0]).getValue() * Math.PI);
            }
        };
        double varX = Math.E;
        Expression e = new ExpressionBuilder("timesPi(log(x^(2+1)))")
                .variables("x")
                .function(custom1)
                .build()
                .setVariable("x", new Number(varX));
        double expected = Math.log(Math.pow(varX, 3)) * Math.PI;
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    // thanks to Marcin Domanski who issued
    // http://jira.congrace.de/jira/browse/EXP-11
    // i have this test, which fails in 0.2.9
    @Test
    public void testFunction18() {
        Function minFunction = new Function("min", 2) {

            @Override
            public Operand apply(Operand[] values) {
                double currentMin = Double.POSITIVE_INFINITY;
                for (Operand value : values) {
                    currentMin = Math.min(currentMin, ((Number)value).getValue());
                }
                return new Number(currentMin);
            }
        };
        ExpressionBuilder b = new ExpressionBuilder("-min(5, 0) + 10")
                .function(minFunction);
        double calculated = ((Number)b.build().evaluate()).getValue();
        assertEquals(10, calculated, 0.0);
    }

    // thanks to Sylvain Machefert who issued
    // http://jira.congrace.de/jira/browse/EXP-11
    // i have this test, which fails in 0.3.2
    @Test
    public void testFunction19() {
        Function minFunction = new Function("power", 2) {

            @Override
            public Operand apply(Operand[] values) {
                return new Number(Math.pow(((Number)values[0]).getValue(), ((Number)values[1]).getValue()));
            }
        };
        ExpressionBuilder b = new ExpressionBuilder("power(2,3)")
                .function(minFunction);
        double calculated = ((Number)b.build().evaluate()).getValue();
        assertEquals(Math.pow(2, 3), calculated, 0d);
    }

    // thanks to Narendra Harmwal who noticed that getArgumentCount was not
    // implemented
    // this test has been added in 0.3.5
    @Test
    public void testFunction20() {
        Function maxFunction = new Function("max", 3) {

            @Override
            public Operand apply(Operand... values) {
                double max = ((Number)values[0]).getValue();
                for (int i = 1; i < numArguments; i++) {
                    if (((Number)values[i]).getValue() > max) {
                        max = ((Number)values[i]).getValue();
                    }
                }
                return new Number(max);
            }
        };
        ExpressionBuilder b = new ExpressionBuilder("max(1,2,3)")
                .function(maxFunction);
        double calculated = ((Number)b.build().evaluate()).getValue();
        assertEquals(3, maxFunction.getNumArguments());
        assertEquals(3, calculated, 0.0);
    }

    @Test
    public void testOperators1() {
        Operator factorial = getFactorialOperator();

        Expression e = new ExpressionBuilder("1!").operator(factorial)
                .build();
        assertEquals(1d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("2!").operator(factorial)
                .build();
        assertEquals(2d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("3!").operator(factorial)
                .build();
        assertEquals(6d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("4!").operator(factorial)
                .build();
        assertEquals(24d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("5!").operator(factorial)
                .build();
        assertEquals(120d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("11!").operator(factorial)
                .build();
        assertEquals(39916800d, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testOperators2() {
        Operator factorial = getFactorialOperator();
        Expression e = new ExpressionBuilder("2^3!").operator(factorial)
                .build();
        assertEquals(64d, ((Number)e.evaluate()).getValue(), 0d);
        e = new ExpressionBuilder("3!^2").operator(factorial)
                .build();
        assertEquals(36d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("-(3!)^-1").operator(factorial)
                .build();
        double actual = ((Number)e.evaluate()).getValue();
        assertEquals(Math.pow(-6d, -1), actual, EPSILON);
    }

    @Test
    public void testOperators3() {
        Operator gteq = new Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

            @Override
            public Operand apply(Operand[] values) {
                if (((Number)values[0]).getValue() >= ((Number)values[1]).getValue()) {
                    return new Number(1d);
                } else {
                    return new Number(0d);
                }
            }
        };
        Expression e = new ExpressionBuilder("1>=2").operator(gteq)
                .build();
        assertEquals(0d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("2>=1").operator(gteq)
                .build();
        assertEquals(1d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("-2>=1").operator(gteq)
                .build();
        assertEquals(0d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("-2>=-1").operator(gteq)
                .build();
        assertEquals(0d, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testModulo1() {
        Operand result = new ExpressionBuilder("33%(20/2)%2")
                .build().evaluate();
        assertEquals(1d, ((Number)result).getValue(), 0.0);
    }

    @Test
    public void testOperators4() {
        Operator greaterEq = new Operator(">=", 2, true, 4) {

            @Override
            public Operand apply(Operand[] values) {
                if (((Number)values[0]).getValue() >= ((Number)values[1]).getValue()) {
                    return new Number(1d);
                } else {
                    return new Number(0d);
                }
            }
        };
        Operator greater = new Operator(">", 2, true, 4) {

            @Override
            public Operand apply(Operand[] values) {
                if (((Number)values[0]).getValue() > ((Number)values[1]).getValue()) {
                    return new Number(1d);
                } else {
                    return new Number(0d);
                }
            }
        };
        Operator newPlus = new Operator(">=>", 2, true, 4) {

            @Override
            public Operand apply(Operand[] values) {
                return new Number(((Number)values[0]).getValue() + ((Number)values[1]).getValue());
            }
        };
        Expression e = new ExpressionBuilder("1>2").operator(greater)
                .build();
        assertEquals(0d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("2>=2").operator(greaterEq)
                .build();
        assertEquals(1d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("1>=>2").operator(newPlus)
                .build();
        assertEquals(3d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("1>=>2>2").operator(greater).operator(newPlus)
                .build();
        assertEquals(1d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("1>=>2>2>=1").operator(greater).operator(newPlus)
                .operator(greaterEq)
                .build();
        assertEquals(1d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("1 >=> 2 > 2 >= 1").operator(greater).operator(newPlus)
                .operator(greaterEq)
                .build();
        assertEquals(1d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("1 >=> 2 >= 2 > 1").operator(greater).operator(newPlus)
                .operator(greaterEq)
                .build();
        assertEquals(0d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("1 >=> 2 >= 2 > 0").operator(greater).operator(newPlus)
                .operator(greaterEq)
                .build();
        assertEquals(1d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("1 >=> 2 >= 2 >= 1").operator(greater).operator(newPlus)
                .operator(greaterEq)
                .build();
        assertEquals(1d, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOperator1() {
        Operator fail = new Operator("2", 2, true, 1) {

            @Override
            public Operand apply(Operand[] values) {
                return new Number(0);
            }
        };
        new ExpressionBuilder("1").operator(fail)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFunction1() {
        Function func = new Function("1gd") {

            @Override
            public Operand apply(Operand... args) {
                return new Number(0);
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFunction2() {
        Function func = new Function("+1gd") {

            @Override
            public Operand apply(Operand... args) {
                return new Number(0);
            }
        };
    }

    @Test
    public void testExpressionBuilder01() {
        Expression e = new ExpressionBuilder("7*x + 3*y")
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(1))
                .setVariable("y", new Number(2));
        Operand result = e.evaluate();
        assertEquals(13d, ((Number)result).getValue(), 0.0);
    }

    @Test
    public void testExpressionBuilder02() {
        Expression e = new ExpressionBuilder("7*x + 3*y")
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(1))
                .setVariable("y", new Number(2));
        Operand result = e.evaluate();
        assertEquals(13d, ((Number)result).getValue(), 0.0);
    }

    @Test
    public void testExpressionBuilder03() {
        double varX = 1.3d;
        double varY = 4.22d;
        Expression e = new ExpressionBuilder("7*x + 3*y - log(y/x*12)^y")
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(varX))
                .setVariable("y", new Number(varY));
        Operand result = e.evaluate();
        assertEquals(((Number)result).getValue(), 7 * varX + 3 * varY - pow(log(varY / varX * 12), varY), EPSILON);
    }

    @Test
    public void testExpressionBuilder04() {
        double varX = 1.3d;
        double varY = 4.22d;
        Expression e =
                new ExpressionBuilder("7*x + 3*y - log(y/x*12)^y")
                        .variables("x", "y")
                        .build()
                        .setVariable("x", new Number(varX))
                        .setVariable("y", new Number(varY));
        Operand result = e.evaluate();
        assertEquals(((Number)result).getValue(), 7 * varX + 3 * varY - pow(log(varY / varX * 12), varY), EPSILON);
        varX = 1.79854d;
        varY = 9281.123d;
        e.setVariable("x", new Number(varX));
        e.setVariable("y", new Number(varY));
        result = e.evaluate();
        assertEquals(((Number)result).getValue(), 7 * varX + 3 * varY - pow(log(varY / varX * 12), varY), 0.0);
    }

    @Test
    public void testExpressionBuilder05() {
        double varX = 1.3d;
        double varY = 4.22d;
        Expression e = new ExpressionBuilder("3*y")
                .variables("y")
                .build()
                .setVariable("x", new Number(varX))
                .setVariable("y", new Number(varY));
        Operand result = e.evaluate();
        assertEquals(((Number)result).getValue(), 3 * varY, EPSILON);
    }

    @Test
    public void testExpressionBuilder06() {
        double varX = 1.3d;
        double varY = 4.22d;
        double varZ = 4.22d;
        Expression e = new ExpressionBuilder("x * y * z")
                .variables("x", "y", "z")
                .build();
        e.setVariable("x", new Number(varX));
        e.setVariable("y", new Number(varY));
        e.setVariable("z", new Number(varZ));
        Operand result = e.evaluate();
        assertEquals(((Number)result).getValue(), varX * varY * varZ, EPSILON);
    }

    @Test
    public void testExpressionBuilder07() {
        double varX = 1.3d;
        Expression e = new ExpressionBuilder("log(sin(x))")
                .variables("x")
                .build()
                .setVariable("x", new Number(varX));
        Operand result = e.evaluate();
        assertEquals(((Number)result).getValue(), log(sin(varX)), EPSILON);
    }

    @Test
    public void testExpressionBuilder08() {
        double varX = 1.3d;
        Expression e = new ExpressionBuilder("log(sin(x))")
                .variables("x")
                .build()
                .setVariable("x", new Number(varX));
        Operand result = e.evaluate();
        assertEquals(((Number)result).getValue(), log(sin(varX)), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameName() {
        Function custom = new Function("bar") {

            @Override
            public Operand apply(Operand... values) {
                return new Number(((Number)values[0]).getValue() / 2);
            }
        };
        double varBar = 1.3d;
        Expression e = new ExpressionBuilder("bar(bar)")
                .variables("bar")
                .function(custom)
                .build()
                .setVariable("bar", new Number(varBar));
        ValidationResult res = e.validate();
        assertFalse(res.isValid());
        assertEquals(1, res.getErrors().size());
    }

    @Test
    public void testInvalidFunction() {
        double varY = 4.22d;
        Expression e = new ExpressionBuilder("3*invalid_function(y)")
                .variables("<")
                .build()
                .setVariable("y", new Number(varY));
        Operand result = e.evaluate();
        assertIsInstruction(result);
    }

    public void testMissingVar() {
        double varY = 4.22d;
        Expression e = new ExpressionBuilder("3*y*z")
                .variables("y", "z")
                .build()
                .setVariable("y", new Number(varY));
        Operand result = e.evaluate();
        assertIsInstruction(result);
    }

    @Test
    public void testUnaryMinusPowerPrecedence() {
        Expression e = new ExpressionBuilder("-1^2")
                .build();
        assertEquals(-1d, ((Number)e.evaluate()).getValue(), 0d);
    }

    @Test
    public void testUnaryMinus() {
        Expression e = new ExpressionBuilder("-1")
                .build();
        assertEquals(-1d, ((Number)e.evaluate()).getValue(), 0d);
    }

    @Test
    public void testExpression1() {
        String expr;
        double expected;
        expr = "2 + 4";
        expected = 6d;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression10() {
        String expr;
        double expected;
        expr = "1 * 1.5 + 1";
        expected = 1 * 1.5 + 1;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression11() {
        double x = 1d;
        double y = 2d;
        String expr = "log(x) ^ sin(y)";
        double expected = Math.pow(Math.log(x), Math.sin(y));
        Expression e = new ExpressionBuilder(expr)
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(x))
                .setVariable("y", new Number(y));
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression12() {
        String expr = "log(2.5333333333)^(0-1)";
        double expected = Math.pow(Math.log(2.5333333333d), -1);
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression13() {
        String expr = "2.5333333333^(0-1)";
        double expected = Math.pow(2.5333333333d, -1);
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression14() {
        String expr = "2 * 17.41 + (12*2)^(0-1)";
        double expected = 2 * 17.41d + Math.pow((12 * 2), -1);
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression15() {
        String expr = "2.5333333333 * 17.41 + (12*2)^log(2.764)";
        double expected = 2.5333333333d * 17.41d + Math.pow((12 * 2), Math.log(2.764d));
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression16() {
        String expr = "2.5333333333/2 * 17.41 + (12*2)^(log(2.764) - sin(5.6664))";
        double expected = 2.5333333333d / 2 * 17.41d + Math.pow((12 * 2), Math.log(2.764d) - Math.sin(5.6664d));
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression17() {
        String expr = "x^2 - 2 * y";
        double x = Math.E;
        double y = Math.PI;
        double expected = x * x - 2 * y;
        Expression e = new ExpressionBuilder(expr)
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(x))
                .setVariable("y", new Number(y));
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression18() {
        String expr = "-3";
        double expected = -3;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression19() {
        String expr = "-3 * -24.23";
        double expected = -3 * -24.23d;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression2() {
        String expr;
        double expected;
        expr = "2+3*4-12";
        expected = 2 + 3 * 4 - 12;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression20() {
        String expr = "-2 * 24/log(2) -2";
        double expected = -2 * 24 / Math.log(2) - 2;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression21() {
        String expr = "-2 *33.34/log(x)^-2 + 14 *6";
        double x = 1.334d;
        double expected = -2 * 33.34 / Math.pow(Math.log(x), -2) + 14 * 6;
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build()
                .setVariable("x", new Number(x));
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpressionPower() {
        String expr = "2^-2";
        double expected = Math.pow(2, -2);
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0d);
    }

    @Test
    public void testExpressionMultiplication() {
        String expr = "2*-2";
        double expected = -4d;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0d);
    }

    @Test
    public void testExpression22() {
        String expr = "-2 *33.34/log(x)^-2 + 14 *6";
        double x = 1.334d;
        double expected = -2 * 33.34 / Math.pow(Math.log(x), -2) + 14 * 6;
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build()
                .setVariable("x", new Number(x));
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression23() {
        String expr = "-2 *33.34/(log(foo)^-2 + 14 *6) - sin(foo)";
        double x = 1.334d;
        double expected = -2 * 33.34 / (Math.pow(Math.log(x), -2) + 14 * 6) - Math.sin(x);
        Expression e = new ExpressionBuilder(expr)
                .variables("foo")
                .build()
                .setVariable("foo", new Number(x));
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression24() {
        String expr = "3+4-log(23.2)^(2-1) * -1";
        double expected = 3 + 4 - Math.pow(Math.log(23.2), (2 - 1)) * -1;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression25() {
        String expr = "+3+4-+log(23.2)^(2-1) * + 1";
        double expected = 3 + 4 - Math.log(23.2d);
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression26() {
        String expr = "14 + -(1 / 2.22^3)";
        double expected = 14 + -(1d / Math.pow(2.22d, 3d));
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression27() {
        String expr = "12^-+-+-+-+-+-+---2";
        double expected = Math.pow(12, -2);
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression28() {
        String expr = "12^-+-+-+-+-+-+---2 * (-14) / 2 ^ -log(2.22323) ";
        double expected = Math.pow(12, -2) * -14 / Math.pow(2, -Math.log(2.22323));
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression29() {
        String expr = "24.3343 % 3";
        double expected = 24.3343 % 3;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testVarName1() {
        String expr = "12.23 * foo.bar";
        Expression e = new ExpressionBuilder(expr)
                .variables("foo.bar")
                .build()
                .setVariable("foo.bar", new Number(1d));
        assertEquals(12.23, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMisplacedSeparator() {
        String expr = "12.23 * ,foo";
        Expression e = new ExpressionBuilder(expr)
                .build()
                .setVariable(",foo", new Number(1d));
        assertEquals(12.23, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidVarName() {
        String expr = "12.23 * @foo";
        Expression e = new ExpressionBuilder(expr)
                .build()
                .setVariable("@foo", new Number(1d));
        assertEquals(12.23, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testVarMap() {
        String expr = "12.23 * foo - bar";
        Map<String, Operand> variables = new HashMap<>();
        variables.put("foo", new Number(2d));
        variables.put("bar", new Number(3.3d));
        Expression e = new ExpressionBuilder(expr)
                .variables(variables.keySet())
                .build()
                .setVariables(variables);
        assertEquals(12.23d * 2d - 3.3d, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfArguments1() {
        String expr = "log(2,2)";
        Expression e = new ExpressionBuilder(expr)
                .build();
        e.evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfArguments2() {
        Function avg = new Function("avg", 4) {

            @Override
            public Operand apply(Operand... args) {
                float sum = 0;
                for (Operand arg : args) {
                    sum += ((Number)arg).getValue();
                }
                return new Number(sum / args.length);
            }
        };
        String expr = "avg(2,2)";
        Expression e = new ExpressionBuilder(expr)
                .build();
        e.evaluate();
    }

    @Test
    public void testExpression3() {
        String expr;
        double expected;
        expr = "2+4*5";
        expected = 2 + 4 * 5;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression30() {
        String expr = "24.3343 % 3 * 20 ^ -(2.334 % log(2 / 14))";
        double expected = 24.3343d % 3 * Math.pow(20, -(2.334 % Math.log(2d / 14d)));
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression31() {
        String expr = "-2 *33.34/log(y_x)^-2 + 14 *6";
        double x = 1.334d;
        double expected = -2 * 33.34 / Math.pow(Math.log(x), -2) + 14 * 6;
        Expression e = new ExpressionBuilder(expr)
                .variables("y_x")
                .build()
                .setVariable("y_x", new Number(x));
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression32() {
        String expr = "-2 *33.34/log(y_2x)^-2 + 14 *6";
        double x = 1.334d;
        double expected = -2 * 33.34 / Math.pow(Math.log(x), -2) + 14 * 6;
        Expression e = new ExpressionBuilder(expr)
                .variables("y_2x")
                .build()
                .setVariable("y_2x", new Number(x));
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression33() {
        String expr = "-2 *33.34/log(_y)^-2 + 14 *6";
        double x = 1.334d;
        double expected = -2 * 33.34 / Math.pow(Math.log(x), -2) + 14 * 6;
        Expression e = new ExpressionBuilder(expr)
                .variables("_y")
                .build()
                .setVariable("_y", new Number(x));
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression34() {
        String expr = "-2 + + (+4) +(4)";
        double expected = -2 + 4 + 4;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression40() {
        String expr = "1e1";
        double expected = 10d;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression41() {
        String expr = "1e-1";
        double expected = 0.1d;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    /*
     * Added tests for expressions with scientific notation see http://jira.congrace.de/jira/browse/EXP-17
     */
    @Test
    public void testExpression42() {
        String expr = "7.2973525698e-3";
        double expected = 7.2973525698e-3d;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression43() {
        String expr = "6.02214E23";
        float expected = 6.02214e23f;
        Expression e = new ExpressionBuilder(expr)
                .build();
        Operand result = e.evaluate();
        float sdf = ((Number)result).getValue();
        assertEquals(expected, sdf, EPSILON);
    }

    @Test
    public void testExpression44() {
        String expr = "6.02214E23";
        float expected = 6.02214e23f;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test(expected = NumberFormatException.class)
    public void testExpression45() {
        String expr = "6.02214E2E3";
        new ExpressionBuilder(expr)
                .build();
    }

    @Test(expected = NumberFormatException.class)
    public void testExpression46() {
        String expr = "6.02214e2E3";
        new ExpressionBuilder(expr)
                .build();
    }

    // tests for EXP-20: No exception is thrown for unmatched parenthesis in
    // build
    // Thanks go out to maheshkurmi for reporting
    @Test(expected = IllegalArgumentException.class)
    public void testExpression48() {
        String expr = "(1*2";
        Expression e = new ExpressionBuilder(expr)
                .build();
        Operand result = e.evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpression49() {
        String expr = "{1*2";
        Expression e = new ExpressionBuilder(expr)
                .build();
        Operand result = e.evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpression50() {
        String expr = "[1*2";
        Expression e = new ExpressionBuilder(expr)
                .build();
        Operand result = e.evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpression51() {
        String expr = "(1*{2+[3}";
        Expression e = new ExpressionBuilder(expr)
                .build();
        Operand result = e.evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpression52() {
        String expr = "(1*(2+(3";
        Expression e = new ExpressionBuilder(expr)
                .build();
        Operand result = e.evaluate();
    }

    @Test
    public void testExpression53() {
        String expr = "14 * 2x";
        Expression exp = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        exp.setVariable("x", new Number(1.5d));
        assertTrue(exp.validate().isValid());
        assertEquals(14d * 2d * 1.5d, ((Number)exp.evaluate()).getValue(), 0d);
    }

    @Test
    public void testExpression54() {
        String expr = "2 ((-(x)))";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        e.setVariable("x", new Number(1.5d));
        assertEquals(-3d, ((Number)e.evaluate()).getValue(), 0d);
    }

    @Test
    public void testExpression55() {
        String expr = "2 sin(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        e.setVariable("x", new Number(2d));
        assertEquals(sin(2d) * 2, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression56() {
        String expr = "2 sin(3x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        e.setVariable("x", new Number(2d));
        assertEquals(sin(6d) * 2d, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testDocumentationExample1() {
        Expression e = new ExpressionBuilder("3 * sin(y) - 2 / (x - 2)")
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(2.3))
                .setVariable("y", new Number(3.14));
        Operand result = e.evaluate();
        double expected = 3 * Math.sin(3.14d) - 2d / (2.3d - 2d);
        assertEquals(expected, ((Number)result).getValue(), EPSILON);
    }

    @Test
    public void testDocumentationExample2() throws Exception {
        ExecutorService exec = Executors.newFixedThreadPool(1);
        Expression e = new ExpressionBuilder("3log(y)/(x+1)")
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(2.3))
                .setVariable("y", new Number(3.14));
        Future<Operand> result = e.evaluateAsync(exec);
        double expected = 3 * Math.log(3.14d) / (3.3);
        assertEquals(expected, ((Number)result.get()).getValue(), EPSILON);
    }

    @Test
    public void testDocumentationExample3() {
        Operand result = new ExpressionBuilder("2cos(xy)")
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(0.5d))
                .setVariable("y", new Number(0.25d))
                .evaluate();
        assertEquals(2d * Math.cos(0.5d * 0.25d), ((Number)result).getValue(), EPSILON);
    }

    @Test
    public void testDocumentationExample4() {
        String expr = "pi+π+e+φ";
        double expected = 2 * Math.PI + Math.E + 1.61803398874d;
        Expression e = new ExpressionBuilder(expr).build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testDocumentationExample5() {
        String expr = "7.2973525698e-3";
        double expected = Double.parseDouble(expr);
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }


    @Test
    public void testDocumentationExample6() {
        Function logb = new Function("logb", 2) {
            @Override
            public Operand apply(Operand... args) {
                return new Number(Math.log(((Number)args[0]).getValue()) / Math.log(((Number)args[1]).getValue()));
            }
        };
        Operand result = new ExpressionBuilder("logb(8, 2)")
                .function(logb)
                .build()
                .evaluate();
        double expected = 3;
        assertEquals(expected, ((Number)result).getValue(), 0d);
    }

    @Test
    public void testDocumentationExample7() {
        Function avg = new Function("avg", 4) {

            @Override
            public Operand apply(Operand... args) {
                double sum = 0;
                for (Operand arg : args) {
                    sum += ((Number)arg).getValue();
                }
                return new Number(sum / args.length);
            }
        };
        Operand result = new ExpressionBuilder("avg(1,2,3,4)")
                .function(avg)
                .build()
                .evaluate();

        double expected = 2.5d;
        assertEquals(expected, ((Number)result).getValue(), 0d);
    }

    @Test
    public void testDocumentationExample8() {
        Operator factorial = getFactorialOperator();

        Operand result = new ExpressionBuilder("3!")
                .operator(factorial)
                .build()
                .evaluate();

        double expected = 6d;
        assertEquals(expected, ((Number)result).getValue(), 0d);
    }

    @Test
    public void testDocumentationExample9() {
        Operator gteq = new Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

            @Override
            public Operand apply(Operand[] values) {
                if (((Number)values[0]).getValue() >= ((Number)values[1]).getValue()) {
                    return new Number(1d);
                } else {
                    return new Number(0d);
                }
            }
        };

        Expression e = new ExpressionBuilder("1>=2").operator(gteq)
                .build();
        assertEquals(0d, ((Number)e.evaluate()).getValue(), 0.0);
        e = new ExpressionBuilder("2>=1").operator(gteq)
                .build();
        assertEquals(1d, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test(expected = ArithmeticException.class)
    public void testDocumentationExample10() {
        Operator reciprocal = new Operator("$", 1, true, Operator.PRECEDENCE_DIVISION) {
            @Override
            public Operand apply(final Operand... args) {
                if (((Number)args[0]).getValue() == 0d) {
                    throw new ArithmeticException("Division by zero!");
                }
                return new Number(1d / ((Number)args[0]).getValue());
            }
        };
        Expression e = new ExpressionBuilder("0$").operator(reciprocal).build();
        e.evaluate();
    }

    @Test
    public void testDocumentationExample11() {
        Expression e = new ExpressionBuilder("x")
                .variable("x")
                .build();

        ValidationResult res = e.validate();
        assertFalse(res.isValid());
        assertEquals(1, res.getErrors().size());

        e.setVariable("x", new Number(1d));
        res = e.validate();
        assertTrue(res.isValid());
    }

    @Test
    public void testDocumentationExample12() {
        Expression e = new ExpressionBuilder("x")
                .variable("x")
                .build();

        ValidationResult res = e.validate(false);
        assertTrue(res.isValid());
        assertNull(res.getErrors());
    }

    // Thanks go out to Johan Björk for reporting the division by zero problem EXP-22
    // https://www.objecthunter.net/jira/browse/EXP-22
    @Test(expected = ArithmeticException.class)
    public void testExpression57() {
        String expr = "1 / 0";
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(Double.POSITIVE_INFINITY, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression58() {
        String expr = "17 * sqrt(-1) * 12";
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertTrue(Double.isNaN(((Number)e.evaluate()).getValue()));
    }

    // Thanks go out to Alex Dolinsky for reporting the missing exception when an empty
    // expression is passed as in new ExpressionBuilder("")
    @Test(expected = IllegalArgumentException.class)
    public void testExpression59() {
        Expression e = new ExpressionBuilder("")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpression60() {
        Expression e = new ExpressionBuilder("   ")
                .build();
        e.evaluate();
    }

    @Test(expected = ArithmeticException.class)
    public void testExpression61() {
        Expression e = new ExpressionBuilder("14 % 0")
                .build();
        e.evaluate();
    }

    // https://www.objecthunter.net/jira/browse/EXP-24
    // thanks go out to Rémi for the issue report
    @Test
    public void testExpression62() {
        Expression e = new ExpressionBuilder("x*1.0e5+5")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(E * 1.0 * pow(10, 5) + 5, ((Number)e.evaluate()).getValue(), BIG_EPSILON);
    }

    @Test
    public void testExpression63() {
        Expression e = new ExpressionBuilder("log10(5)")
                .build();
        assertEquals(Math.log10(5), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression64() {
        Expression e = new ExpressionBuilder("log2(5)")
                .build();
        assertEquals(Math.log(5) / Math.log(2), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression65() {
        Expression e = new ExpressionBuilder("2log(e)")
                .variables("e")
                .build()
                .setVariable("e", new Number(Math.E));

        assertEquals(2d, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression66() {
        Expression e = new ExpressionBuilder("log(e)2")
                .variables("e")
                .build()
                .setVariable("e", new Number(Math.E));

        assertEquals(2d, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression67() {
        Expression e = new ExpressionBuilder("2esin(pi/2)")
                .variables("e", "pi")
                .build()
                .setVariable("e", new Number(Math.E))
                .setVariable("pi", new Number(Math.PI));

        assertEquals(2 * Math.E * Math.sin(Math.PI / 2d), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression68() {
        Expression e = new ExpressionBuilder("2x")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(2 * Math.E, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression69() {
        Expression e = new ExpressionBuilder("2x2")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(4 * Math.E, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression70() {
        Expression e = new ExpressionBuilder("2xx")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(2 * Math.E * Math.E, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression71() {
        Expression e = new ExpressionBuilder("x2x")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(2 * Math.E * Math.E, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression72() {
        Expression e = new ExpressionBuilder("2cos(x)")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(2 * Math.cos(Math.E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression73() {
        Expression e = new ExpressionBuilder("cos(x)2")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(2 * Math.cos(Math.E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression74() {
        Expression e = new ExpressionBuilder("cos(x)(-2)")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(-2d * Math.cos(Math.E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression75() {
        Expression e = new ExpressionBuilder("(-2)cos(x)")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(-2d * Math.cos(Math.E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression76() {
        Expression e = new ExpressionBuilder("(-x)cos(x)")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(-E * Math.cos(Math.E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression77() {
        Expression e = new ExpressionBuilder("(-xx)cos(x)")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(-E * E * Math.cos(Math.E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression78() {
        Expression e = new ExpressionBuilder("(xx)cos(x)")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(E * E * Math.cos(Math.E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression79() {
        Expression e = new ExpressionBuilder("cos(x)(xx)")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(E * E * Math.cos(Math.E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression80() {
        Expression e = new ExpressionBuilder("cos(x)(xy)")
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(Math.E))
                .setVariable("y", new Number(Math.sqrt(2)));
        assertEquals(sqrt(2) * E * Math.cos(Math.E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression81() {
        Expression e = new ExpressionBuilder("cos(xy)")
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(Math.E))
                .setVariable("y", new Number(Math.sqrt(2)));
        assertEquals(cos(sqrt(2) * E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression82() {
        Expression e = new ExpressionBuilder("cos(2x)")
                .variables("x")
                .build()
                .setVariable("x", new Number(Math.E));
        assertEquals(cos(2 * E), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression83() {
        Expression e = new ExpressionBuilder("cos(xlog(xy))")
                .variables("x", "y")
                .build()
                .setVariable("x", new Number(Math.E))
                .setVariable("y", new Number(Math.sqrt(2)));
        assertEquals(cos(E * log(E * sqrt(2))), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression84() {
        Expression e = new ExpressionBuilder("3x_1")
                .variables("x_1")
                .build()
                .setVariable("x_1", new Number(Math.E));
        assertEquals(3d * E, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testExpression85() {
        Expression e = new ExpressionBuilder("1/2x")
                .variables("x")
                .build()
                .setVariable("x", new Number(6));
        assertEquals(3d, ((Number)e.evaluate()).getValue(), 0d);
    }

    // thanks got out to David Sills
    @Test(expected = IllegalArgumentException.class)
    public void testSpaceBetweenNumbers() {
        Expression e = new ExpressionBuilder("1 1")
                .build();
    }

    // thanks go out to Janny for providing the tests and the bug report
    @Test
    public void testUnaryMinusInParenthesisSpace() {
        ExpressionBuilder b = new ExpressionBuilder("( -1)^2");
        Operand calculated = b.build().evaluate();
        assertEquals(1d, ((Number)calculated).getValue(), 0.0);
    }

    @Test
    public void testUnaryMinusSpace() {
        ExpressionBuilder b = new ExpressionBuilder(" -1 + 2");
        Operand calculated = b.build().evaluate();
        assertEquals(1d, ((Number)calculated).getValue(), 0.0);
    }

    @Test
    public void testUnaryMinusSpaces() {
        ExpressionBuilder b = new ExpressionBuilder(" -1 + + 2 +   -   1");
        Operand calculated = b.build().evaluate();
        assertEquals(0d, ((Number)calculated).getValue(), 0.0);
    }

    @Test
    public void testUnaryMinusSpace1() {
        ExpressionBuilder b = new ExpressionBuilder("-1");
        Operand calculated = b.build().evaluate();
        assertEquals(((Number)calculated).getValue(), -1d, 0.0);
    }

    @Test
    public void testExpression4() {
        String expr;
        double expected;
        expr = "2+4 * 5";
        expected = 2 + 4 * 5;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression5() {
        String expr;
        double expected;
        expr = "(2+4)*5";
        expected = (2 + 4) * 5;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression6() {
        String expr;
        double expected;
        expr = "(2+4)*5 + 2.5*2";
        expected = (2 + 4) * 5 + 2.5 * 2;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression7() {
        String expr;
        double expected;
        expr = "(2+4)*5 + 10/2";
        expected = (2 + 4) * 5 + 10 / 2;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression8() {
        String expr;
        double expected;
        expr = "(2 * 3 +4)*5 + 10/2";
        expected = (2 * 3 + 4) * 5 + 10 / 2;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testExpression9() {
        String expr;
        double expected;
        expr = "(2 * 3 +4)*5 +4 + 10/2";
        expected = 59; //(2 * 3 + 4) * 5 + 4 + 10 / 2 = 59
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testFailUnknownFunction1() {
        String expr;
        expr = "lig(1)";
        Expression e = new ExpressionBuilder(expr)
                .build();
        Operand result = e.evaluate();
        assertIsInstruction(result);
    }

    @Test
    public void testFailUnknownFunction2() {
        String expr;
        expr = "galength(1)";
        Operand result = new ExpressionBuilder(expr)
                .build().evaluate();
        assertIsInstruction(result);
    }

    @Test
    public void testFailUnknownFunction3() {
        String expr;
        expr = "tcos(1)";
        Expression exp = new ExpressionBuilder(expr)
                .build();
        Operand result = exp.evaluate();
        assertIsInstruction(result);
    }

    @Test
    public void testFunction22() {
        String expr;
        expr = "cos(cos_1)";
        Expression e = new ExpressionBuilder(expr)
                .variables("cos_1")
                .build()
                .setVariable("cos_1", new Number(1d));
        assertEquals(((Number)e.evaluate()).getValue(), cos(1d), EPSILON);
    }

    @Test
    public void testFunction23() {
        String expr;
        expr = "log1p(1)";
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(log1p(1d), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testFunction24() {
        String expr;
        expr = "pow(3,3)";
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(27d, ((Number)e.evaluate()).getValue(), 0d);
    }

    @Test
    public void testPostfix1() {
        String expr;
        double expected;
        expr = "2.2232^0.1";
        expected = Math.pow(2.2232d, 0.1d);
        Operand actual = new ExpressionBuilder(expr)
                .build().evaluate();
        assertEquals(expected, ((Number)actual).getValue(), EPSILON);
    }

    @Test
    public void testPostfixEverything() {
        String expr;
        double expected;
        expr = "(sin(12) + log(34)) * 3.42 - cos(2.234-log(2))";
        expected = (Math.sin(12) + Math.log(34)) * 3.42 - Math.cos(2.234 - Math.log(2));
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testPostfixExponentiation1() {
        String expr;
        double expected;
        expr = "2^3";
        expected = Math.pow(2, 3);
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testPostfixExponentiation2() {
        String expr;
        double expected;
        expr = "24 + 4 * 2^3";
        expected = 24 + 4 * Math.pow(2, 3);
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testPostfixExponentiation3() {
        String expr;
        double expected;
        double x = 4.334d;
        expr = "24 + 4 * 2^x";
        expected = 24 + 4 * Math.pow(2, x);
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build()
                .setVariable("x", new Number(x));
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testPostfixExponentiation4() {
        String expr;
        double expected;
        double x = 4.334d;
        expr = "(24 + 4) * 2^log(x)";
        expected = (24 + 4) * Math.pow(2, Math.log(x));
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build()
                .setVariable("x", new Number(x));
        assertEquals(expected, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testPostfixFunction1() {
        String expr;
        double expected;
        expr = "log(1) * sin(0)";
        expected = Math.log(1) * Math.sin(0);
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testPostfixFunction10() {
        String expr;
        double expected;
        expr = "cbrt(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected = Math.cbrt(x);
            assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
        }
    }

    @Test
    public void testPostfixFunction11() {
        String expr;
        double expected;
        expr = "cos(x) - (1/cbrt(x))";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            if (x == 0d) continue;
            expected = Math.cos(x) - (1 / Math.cbrt(x));
            assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
        }
    }

    @Test
    public void testPostfixFunction12() {
        String expr;
        double expected;
        expr = "acos(x) * expm1(asin(x)) - exp(atan(x)) + floor(x) + cosh(x) - sinh(cbrt(x))";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected =
                    Math.acos(x) * Math.expm1(Math.asin(x)) - Math.exp(Math.atan(x)) + Math.floor(x) + Math.cosh(x)
                            - Math.sinh(Math.cbrt(x));
            if (Double.isNaN(expected)) {
                assertTrue(Double.isNaN(((Number)e.setVariable("x", new Number(x)).evaluate()).getValue()));
            } else {
                assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
            }
        }
    }

    @Test
    public void testPostfixFunction13() {
        String expr;
        double expected;
        expr = "acos(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected = Math.acos(x);
            if (Double.isNaN(expected)) {
                assertTrue(Double.isNaN(((Number)e.setVariable("x", new Number(x)).evaluate()).getValue()));
            } else {
                assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
            }
        }
    }

    @Test
    public void testPostfixFunction14() {
        String expr;
        double expected;
        expr = " expm1(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected = Math.expm1(x);
            if (Double.isNaN(expected)) {
                assertTrue(Double.isNaN(((Number)e.setVariable("x", new Number(x)).evaluate()).getValue()));
            } else {
                assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
            }
        }
    }

    @Test
    public void testPostfixFunction15() {
        String expr;
        double expected;
        expr = "asin(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected = Math.asin(x);
            if (Double.isNaN(expected)) {
                assertTrue(Double.isNaN(((Number)e.setVariable("x", new Number(x)).evaluate()).getValue()));
            } else {
                assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
            }
        }
    }

    @Test
    public void testPostfixFunction16() {
        String expr;
        double expected;
        expr = " exp(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected = Math.exp(x);
            assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
        }
    }

    @Test
    public void testPostfixFunction17() {
        String expr;
        double expected;
        expr = "floor(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected = Math.floor(x);
            assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), 0.0);
        }
    }

    @Test
    public void testPostfixFunction18() {
        String expr;
        double expected;
        expr = " cosh(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected = Math.cosh(x);
            assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
        }
    }

    @Test
    public void testPostfixFunction19() {
        String expr;
        double expected;
        expr = "sinh(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected = Math.sinh(x);
            assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
        }
    }

    @Test
    public void testPostfixFunction20() {
        String expr;
        double expected;
        expr = "cbrt(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected = Math.cbrt(x);
            assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
        }
    }

    @Test
    public void testPostfixFunction21() {
        String expr;
        double expected;
        expr = "tanh(x)";
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        for (double x = -10; x < 10; x = x + 0.5d) {
            expected = Math.tanh(x);
            assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
        }
    }

    @Test
    public void testPostfixFunction2() {
        String expr;
        double expected;
        expr = "log(1)";
        expected = 0d;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testPostfixFunction3() {
        String expr;
        double expected;
        expr = "sin(0)";
        expected = 0d;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testPostfixFunction5() {
        String expr;
        double expected;
        expr = "ceil(2.3) +1";
        expected = Math.ceil(2.3) + 1;
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testPostfixFunction6() {
        String expr;
        double expected;
        double x = 1.565d;
        double y = 2.1323d;
        expr = "ceil(x) + 1 / y * abs(1.4)";
        expected = Math.ceil(x) + 1 / y * Math.abs(1.4);
        Expression e = new ExpressionBuilder(expr)
                .variables("x", "y")
                .build();
        assertEquals(expected, ((Number)e.setVariable("x", new Number(x))
                .setVariable("y", new Number(y)).evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testPostfixFunction7() {
        String expr;
        double expected;
        double x = Math.E;
        expr = "tan(x)";
        expected = Math.tan(x);
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testPostfixFunction8() {
        String expr;
        double expected;
        expr = "2^3.4223232 + tan(e)";
        expected = Math.pow(2, 3.4223232d) + Math.tan(Math.E);
        Expression e = new ExpressionBuilder(expr)
                .variables("e")
                .build();
        assertEquals(expected, ((Number)e.setVariable("e", new Number(E)).evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testPostfixFunction9() {
        String expr;
        double expected;
        double x = Math.E;
        expr = "cbrt(x)";
        expected = Math.cbrt(x);
        Expression e = new ExpressionBuilder(expr)
                .variables("x")
                .build();
        assertEquals(expected, ((Number)e.setVariable("x", new Number(x)).evaluate()).getValue(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPostfixInvalidVariableName() {
        String expr;
        double expected;
        double x = 4.5334332d;
        double log = Math.PI;
        expr = "x * pi";
        expected = x * log;
        Expression e = new ExpressionBuilder(expr)
                .variables("x", "pi")
                .build();
        assertEquals(expected, ((Number)e.setVariable("x", new Number(x))
                .setVariable("log", new Number(log)).evaluate()).getValue(), 0.0);
    }

    @Test
    public void testPostfixParenthesis() {
        String expr;
        double expected;
        expr = "(3 + 3 * 14) * (2 * (24-17) - 14)/((34) -2)";
        expected = 0; //(3 + 3 * 14) * (2 * (24-17) - 14)/((34) -2) = 0
        Expression e = new ExpressionBuilder(expr)
                .build();
        assertEquals(expected, ((Number)e.evaluate()).getValue(), 0.0);
    }

    @Test
    public void testPostfixVariables() {
        String expr;
        double expected;
        double x = 4.5334332d;
        double pi = Math.PI;
        expr = "x * pi";
        expected = x * pi;
        Expression e = new ExpressionBuilder(expr)
                .variables("x", "pi")
                .build();
        assertEquals(expected, ((Number)e.setVariable("x", new Number(x))
                .setVariable("pi", new Number(pi)).evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testUnicodeVariable1() {
        Expression e = new ExpressionBuilder("λ")
                .variable("λ")
                .build()
                .setVariable("λ", new Number(E));
        assertEquals(E, ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testUnicodeVariable2() {
        Expression e = new ExpressionBuilder("log(3ε+1)")
                .variable("ε")
                .build()
                .setVariable("ε", new Number(E));
        assertEquals(log(3 * E + 1), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testUnicodeVariable3() {
        Function log = new Function("λωγ", 1) {

            @Override
            public Operand apply(Operand... args) {
                return new Number(log(((Number)args[0]).getValue()));
            }
        };

        Expression e = new ExpressionBuilder("λωγ(π)")
                .variable("π")
                .function(log)
                .build()
                .setVariable("π", new Number(PI));
        assertEquals(log(PI), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test
    public void testUnicodeVariable4() {
        Function log = new Function("λ_ωγ", 1) {

            @Override
            public Operand apply(Operand... args) {
                return new Number(log(((Number)args[0]).getValue()));
            }
        };

        Expression e = new ExpressionBuilder("3λ_ωγ(πε6)")
                .variables("π", "ε")
                .function(log)
                .build()
                .setVariable("π", new Number(PI))
                .setVariable("ε", new Number(E));
        assertEquals(3 * log(PI * E * 6), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImplicitMultiplicationOffNumber() {
        Expression e = new ExpressionBuilder("var_12")
                .variable("var_1")
                .implicitMultiplication(false)
                .build();
        e.evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImplicitMultiplicationOffVariable() {
        Expression e = new ExpressionBuilder("var_1var_1")
                .variable("var_1")
                .implicitMultiplication(false)
                .build();
        e.evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImplicitMultiplicationOffParentheses() {
        Expression e = new ExpressionBuilder("var_1(2)")
                .variable("var_1")
                .implicitMultiplication(false)
                .build();
        e.evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImplicitMultiplicationOffFunction() {
        Expression e = new ExpressionBuilder("var_1log(2)")
                .variable("var_1")
                .implicitMultiplication(false)
                .build()
                .setVariable("var_1", new Number(2));
        e.evaluate();
    }

    @Test
    public void testImplicitMultiplicationOnNumber() {
        Expression e = new ExpressionBuilder("var_12")
                .variable("var_1")
                .build()
                .setVariable("var_1", new Number(2));
        assertEquals(4d, ((Number)e.evaluate()).getValue(), 0d);
    }

    @Test
    public void testImplicitMultiplicationOnVariable() {
        Expression e = new ExpressionBuilder("var_1var_1")
                .variable("var_1")
                .build()
                .setVariable("var_1", new Number(2));
        assertEquals(4d, ((Number)e.evaluate()).getValue(), 0d);
    }

    @Test
    public void testImplicitMultiplicationOnParentheses() {
        Expression e = new ExpressionBuilder("var_1(2)")
                .variable("var_1")
                .build()
                .setVariable("var_1", new Number(2));
        assertEquals(4d, ((Number)e.evaluate()).getValue(), 0d);
    }

    @Test
    public void testImplicitMultiplicationOnFunction() {
        Expression e = new ExpressionBuilder("var_1log(2)")
                .variable("var_1")
                .build()
                .setVariable("var_1", new Number(2));
        assertEquals(2 * log(2), ((Number)e.evaluate()).getValue(), EPSILON);
    }

    // thanks go out to vandanagopal for reporting the issue
    // https://github.com/fasseg/exp4j/issues/23
    @Test
    public void testSecondArgumentNegative() {
        Function round = new Function("MULTIPLY", 2) {
            @Override
            public Operand apply(Operand... args) {
                return new Number(Math.round(((Number)args[0]).getValue() * ((Number)args[1]).getValue()));
            }
        };
        Operand result = new ExpressionBuilder("MULTIPLY(2,-1)")
                .function(round)
                .build()
                .evaluate();
        assertEquals(-2d, ((Number)result).getValue(), 0d);
    }

    // Test for https://github.com/fasseg/exp4j/issues/65
    @Test
    public void testVariableWithDot() {
        Operand result = new ExpressionBuilder("2*SALARY.Basic")
                .variable("SALARY.Basic")
                .build()
                .setVariable("SALARY.Basic", new Number(1.5d))
                .evaluate();
        assertEquals(3d, ((Number)result).getValue(), 0d);
    }

    @Test
    public void testTwoAdjacentOperators() {
        final Operator factorial = getFactorialOperator();

        Operand result = new ExpressionBuilder("3!+2")
                .operator(factorial)
                .build()
                .evaluate();

        double expected = 8d;
        assertEquals(expected, ((Number)result).getValue(), 0d);
    }

    @Test
    public void testGetVariableNames1() {
        Expression e = new ExpressionBuilder("b*a-9.24c")
                .variables("b", "a", "c")
                .build();
        Set<String> variableNames = e.getVariableNames();
        assertTrue(variableNames.contains("a"));
        assertTrue(variableNames.contains("b"));
        assertTrue(variableNames.contains("c"));
    }

    @Test
    public void testGetVariableNames2() {
        Expression e = new ExpressionBuilder("log(bar)-FOO.s/9.24c")
                .variables("bar", "FOO.s", "c")
                .build();
        Set<String> variableNames = e.getVariableNames();
        assertTrue(variableNames.contains("bar"));
        assertTrue(variableNames.contains("FOO.s"));
        assertTrue(variableNames.contains("c"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameVariableAndBuiltinFunctionName() {
        Expression e = new ExpressionBuilder("log10(log10)")
                .variables("log10")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameVariableAndUserFunctionName() {
        Expression e = new ExpressionBuilder("2*tr+tr(2)")
                .variables("tr")
                .function(new Function("tr") {
                    @Override
                    public Operand apply(Operand... args) {
                        return new Number(0);
                    }
                })
                .build();
    }

    @Test
    public void testSignum() {
        Expression e = new ExpressionBuilder("signum(1)")
                .build();
        assertEquals(1, ((Number)e.evaluate()).getValue(), 0d);

        e = new ExpressionBuilder("signum(-1)")
                .build();
        assertEquals(-1, ((Number)e.evaluate()).getValue(), 0d);

        e = new ExpressionBuilder("signum(--1)")
                .build();
        assertEquals(1, ((Number)e.evaluate()).getValue(), 0d);

        e = new ExpressionBuilder("signum(+-1)")
                .build();
        assertEquals(-1, ((Number)e.evaluate()).getValue(), 0d);

        e = new ExpressionBuilder("-+1")
                .build();
        assertEquals(-1, ((Number)e.evaluate()).getValue(), 0d);

        e = new ExpressionBuilder("signum(-+1)")
                .build();
        assertEquals(-1, ((Number)e.evaluate()).getValue(), 0d);
    }

    @Test
    public void testCustomPercent() {
        Function percentage = new Function("percentage", 2) {
            @Override
            public Operand apply(Operand... args) {
                double val = ((Number)args[0]).getValue();
                double percent = ((Number)args[1]).getValue();
                if (percent < 0) {
                    return new Number(val - val * Math.abs(percent) / 100d);
                } else {
                    return new Number(val - val * percent / 100d);
                }
            }
        };

        Expression e = new ExpressionBuilder("percentage(1000,-10)")
                .function(percentage)
                .build();
        assertEquals(0d, 900, ((Number)e.evaluate()).getValue());

        e = new ExpressionBuilder("percentage(1000,12)")
                .function(percentage)
                .build();
        assertEquals(0d, 1000d * 0.12d, ((Number)e.evaluate()).getValue());
    }

}
