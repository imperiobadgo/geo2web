package parser;

import de.geo2web.parser.Expression;
import de.geo2web.parser.ExpressionBuilder;
import de.geo2web.parser.Number;
import de.geo2web.parser.Operand;
import de.geo2web.parser.function.Functions;
import de.geo2web.parser.operator.Operator;
import de.geo2web.parser.operator.Operators;
import de.geo2web.parser.tokenizer.*;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;
import static parser.TestUtil.getFactorialOperator;


public class ExpressionTest {
    @Test
    public void testExpression1() {
        Token[] tokens = new Token[]{
                new NumberToken(3d),
                new NumberToken(2d),
                new OperatorToken(Operators.getBuiltinOperator('+', 2))
        };
        Expression exp = new Expression(tokens);
        assertEquals(5d, ((Number) exp.evaluate()).getValue(), 0d);
    }

    @Test
    public void testExpression2() {
        Token[] tokens = new Token[]{
                new NumberToken(1d),
                new FunctionToken(Functions.getBuiltinFunction("log")),
        };
        Expression exp = new Expression(tokens);
        assertEquals(0d, ((Number) exp.evaluate()).getValue(), 0d);
    }

    @Test
    public void testGetVariableNames1() {
        Token[] tokens = new Token[]{
                new VariableToken("a"),
                new VariableToken("b"),
                new OperatorToken(Operators.getBuiltinOperator('+', 2))
        };
        Expression exp = new Expression(tokens);

        assertEquals(2, exp.getVariableNames().size());
    }

    @Test
    public void testFactorial() {
        Operator factorial = getFactorialOperator();

        Expression e = new ExpressionBuilder("2!+3!")
                .operator(factorial)
                .build();
        assertEquals(8d, ((Number) e.evaluate()).getValue(), 0d);

        e = new ExpressionBuilder("3!-2!")
                .operator(factorial)
                .build();
        assertEquals(4d, ((Number) e.evaluate()).getValue(), 0d);

        e = new ExpressionBuilder("3!")
                .operator(factorial)
                .build();
        assertEquals(6, ((Number) e.evaluate()).getValue(), 0);

        e = new ExpressionBuilder("3!!")
                .operator(factorial)
                .build();
        assertEquals(720, ((Number) e.evaluate()).getValue(), 0);

        e = new ExpressionBuilder("4 + 3!")
                .operator(factorial)
                .build();
        assertEquals(10, ((Number) e.evaluate()).getValue(), 0);

        e = new ExpressionBuilder("3! * 2")
                .operator(factorial)
                .build();
        assertEquals(12, ((Number) e.evaluate()).getValue(), 0);

        e = new ExpressionBuilder("3!")
                .operator(factorial)
                .build();
        assertTrue(e.validate().isValid());
        assertEquals(6, ((Number) e.evaluate()).getValue(), 0);

        e = new ExpressionBuilder("3!!")
                .operator(factorial)
                .build();
        assertTrue(e.validate().isValid());
        assertEquals(720, ((Number) e.evaluate()).getValue(), 0);

        e = new ExpressionBuilder("4 + 3!")
                .operator(factorial)
                .build();
        assertTrue(e.validate().isValid());
        assertEquals(10, ((Number) e.evaluate()).getValue(), 0);

        e = new ExpressionBuilder("3! * 2")
                .operator(factorial)
                .build();
        assertTrue(e.validate().isValid());
        assertEquals(12, ((Number) e.evaluate()).getValue(), 0);

        e = new ExpressionBuilder("2 * 3!")
                .operator(factorial)
                .build();
        assertTrue(e.validate().isValid());
        assertEquals(12, ((Number) e.evaluate()).getValue(), 0);

        e = new ExpressionBuilder("4 + (3!)")
                .operator(factorial)
                .build();
        assertTrue(e.validate().isValid());
        assertEquals(10, ((Number) e.evaluate()).getValue(), 0);

        e = new ExpressionBuilder("4 + 3! + 2 * 6")
                .operator(factorial)
                .build();
        assertTrue(e.validate().isValid());
        assertEquals(22, ((Number) e.evaluate()).getValue(), 0);
    }

    @Test
    public void testCotangent1() {
        Expression e = new ExpressionBuilder("cot(1)")
                .build();
        assertEquals((float)(1 / Math.tan(1)), ((Number) e.evaluate()).getValue(), 0f);

    }

    @Test(expected = ArithmeticException.class)
    public void testInvalidCotangent1() {
        Expression e = new ExpressionBuilder("cot(0)")
                .build();
        e.evaluate();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testOperatorFactorial2() {
        Operator factorial = getFactorialOperator();

        Expression e = new ExpressionBuilder("!3").build();
        assertFalse(e.validate().isValid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFactorial2() {
        Operator factorial = getFactorialOperator();

        Expression e = new ExpressionBuilder("!!3").build();
        assertFalse(e.validate().isValid());
    }

    @Test
    public void testClearVariables() {
        ExpressionBuilder builder = new ExpressionBuilder("x + y");
        builder.variable("x");
        builder.variable("y");

        Expression expression = builder.build();
        HashMap<String, Operand> values = new HashMap<>();
        values.put("x", new Number(1.0));
        values.put("y", new Number(2.0));
        expression.setVariables(values);


        Operand result = expression.evaluate();
        assertEquals(3.0, ((Number) result).getValue(), 0d);

        expression.clearVariables();

        try {
            result = expression.evaluate();
            fail("Should fail as there aren't values in the expression.");
        } catch (Exception ignored) {

        }

        HashMap<String, Operand> emptyMap = new HashMap<>();
        expression.setVariables(emptyMap);

        try {
            result = expression.evaluate();
            fail("Should fail as there aren't values in the expression.");
        } catch (Exception ignored) {

        }

    }


    @Test
    @Ignore
    // If Expression should be threads safe this test must pass
    public void evaluateFamily() {
        final Expression e = new ExpressionBuilder("sin(x)")
                .variable("x")
                .build();
        Executor executor = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100000; i++) {
            executor.execute(() -> {
                double x = Math.random();
                e.setVariable("x", new Number(x));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                assertEquals(Math.sin(x), ((Number) e.evaluate()).getValue(), 0f);
            });
        }
    }
}
