package parser;

import de.geo2web.parser.Expression;
import de.geo2web.parser.ExpressionBuilder;
import de.geo2web.parser.Number;
import de.geo2web.parser.Operand;
import de.geo2web.parser.ValidationResult;
import de.geo2web.parser.function.Function;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpressionValidateTest {

    /**
     * Dummy function with 2 arguments.
     */
    private final Function beta = new Function("beta", 2) {

        @Override
        public Operand apply(Operand... args) {
            return new Number(((Number)args[1]).getValue() - ((Number)args[0]).getValue());
        }
    };

    /**
     * Dummy function with 3 arguments.
     */
    private final Function gamma = new Function("gamma", 3) {

        @Override
        public Operand apply(Operand... args) {
            return new Number(
                    ((Number)args[0]).getValue()
                    * ((Number)args[1]).getValue()
                    / ((Number)args[2]).getValue());
        }
    };

    /**
     * Dummy function with 7 arguments.
     */
    private final Function eta = new Function("eta", 7) {

        @Override
        public Operand apply(Operand... args) {
            float eta = 0;
            for (Operand a : args) {
                eta += ((Number)a).getValue();
            }
            return new Number(eta);
        }
    };

    // valid scenarios

    @Test
    public void testValidateNumber() {
        Expression exp = new ExpressionBuilder("1")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateNumberPositive() {
        Expression exp = new ExpressionBuilder("+1")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateNumberNegative() {
        Expression exp = new ExpressionBuilder("-1")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateOperator() {
        Expression exp = new ExpressionBuilder("x + 1 + 2")
                .variable("x")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunction() {
        Expression exp = new ExpressionBuilder("sin(x)")
                .variable("x")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionPositive() {
        Expression exp = new ExpressionBuilder("+sin(x)")
                .variable("x")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionNegative() {
        Expression exp = new ExpressionBuilder("-sin(x)")
                .variable("x")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionAndOperator() {
        Expression exp = new ExpressionBuilder("sin(x + 1 + 2)")
                .variable("x")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionWithTwoArguments() {
        Expression exp = new ExpressionBuilder("beta(x, y)")
                .variables("x", "y")
                .functions(beta)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionWithTwoArgumentsAndOperator() {
        Expression exp = new ExpressionBuilder("beta(x, y + 1)")
                .variables("x", "y")
                .functions(beta)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionWithThreeArguments() {
        Expression exp = new ExpressionBuilder("gamma(x, y, z)")
                .variables("x", "y", "z")
                .functions(gamma)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionWithThreeArgumentsAndOperator() {
        Expression exp = new ExpressionBuilder("gamma(x, y, z + 1)")
                .variables("x", "y", "z")
                .functions(gamma)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionWithTwoAndThreeArguments() {
        Expression exp = new ExpressionBuilder("gamma(x, beta(y, h), z)")
                .variables("x", "y", "z", "h")
                .functions(gamma, beta)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionWithTwoAndThreeArgumentsAndOperator() {
        Expression exp = new ExpressionBuilder("gamma(x, beta(y, h), z + 1)")
                .variables("x", "y", "z", "h")
                .functions(gamma, beta)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionWithTwoAndThreeArgumentsAndMultipleOperator() {
        Expression exp = new ExpressionBuilder("gamma(x * 2 / 4, beta(y, h + 1 + 2), z + 1 + 2 + 3 + 4)")
                .variables("x", "y", "z", "h")
                .functions(gamma, beta)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionWithSevenArguments() {
        Expression exp = new ExpressionBuilder("eta(1, 2, 3, 4, 5, 6, 7)")
                .functions(eta)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateFunctionWithSevenArgumentsAndOperator() {
        Expression exp = new ExpressionBuilder("eta(1, 2, 3, 4, 5, 6, 7) * 2 * 3 * 4")
                .functions(eta)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    // invalid scenarios

    @Test
    public void testValidateInvalidFunction() {
        Expression exp = new ExpressionBuilder("sin()")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidOperand() {
        Expression exp = new ExpressionBuilder("1 + ")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidFunctionWithTooFewArguments() {
        Expression exp = new ExpressionBuilder("beta(1)")
                .functions(beta)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidFunctionWithTooFewArgumentsAndOperands() {
        Expression exp = new ExpressionBuilder("beta(1 + )")
                .functions(beta)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidFunctionWithManyArguments() {
        Expression exp = new ExpressionBuilder("beta(1, 2, 3)")
                .functions(beta)
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidOperator() {
        Expression exp = new ExpressionBuilder("+")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    // Thanks go out to werwiesel for reporting the issue
    // https://github.com/fasseg/exp4j/issues/59
    @Test
    public void testNoArgFunctionValidation() {
        Function now = new Function("now", 0) {
            @Override
            public Operand apply(Operand... args) {
                return new Number((double) new Date().getTime());
            }
        };
        Expression e = new ExpressionBuilder("14*now()")
                .function(now)
                .build();
        assertTrue(e.validate().isValid());

        e = new ExpressionBuilder("now()")
                .function(now)
                .build();
        assertTrue(e.validate().isValid());

        e = new ExpressionBuilder("sin(now())")
                .function(now)
                .build();
        assertTrue(e.validate().isValid());

        e = new ExpressionBuilder("sin(now()) % 14")
                .function(now)
                .build();
        assertTrue(e.validate().isValid());
    }


}
