package parser;

import de.geo2web.parser.Number;
import de.geo2web.parser.Operand;
import de.geo2web.parser.ShuntingYard;
import de.geo2web.parser.operator.Operator;
import de.geo2web.parser.tokenizer.Token;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static parser.TestUtil.*;

public class ShuntingYardTest {

    @Test
    public void testShuntingYard1() {
        String expression = "2+3";
        Token[] tokens = ShuntingYard.convertToRPN(expression, null, null, null, true);
        assertNumberToken(tokens[0], new Number(2d));
        assertNumberToken(tokens[1], new Number(3d));
        assertOperatorToken(tokens[2], "+", 2, Operator.PRECEDENCE_ADDITION);
    }

    @Test
    public void testShuntingYard2() {
        String expression = "3*x";
        Token[] tokens = ShuntingYard.convertToRPN(expression, null, null, new HashSet<>(Collections.singletonList("x")), true);
        assertNumberToken(tokens[0], new Number(3d));
        assertVariableToken(tokens[1], "x");
        assertOperatorToken(tokens[2], "*", 2, Operator.PRECEDENCE_MULTIPLICATION);
    }

    @Test
    public void testShuntingYard3() {
        String expression = "-3";
        Token[] tokens = ShuntingYard.convertToRPN(expression, null, null, null, true);
        assertNumberToken(tokens[0], new Number(3d));
        assertOperatorToken(tokens[1], "-", 1, Operator.PRECEDENCE_UNARY_MINUS);
    }

    @Test
    public void testShuntingYard4() {
        String expression = "-2^2";
        Token[] tokens = ShuntingYard.convertToRPN(expression, null, null, null, true);
        assertNumberToken(tokens[0], new Number(2d));
        assertNumberToken(tokens[1], new Number(2d));
        assertOperatorToken(tokens[2], "^", 2, Operator.PRECEDENCE_POWER);
        assertOperatorToken(tokens[3], "-", 1, Operator.PRECEDENCE_UNARY_MINUS);
    }

    @Test
    public void testShuntingYard5() {
        String expression = "2^-2";
        Token[] tokens = ShuntingYard.convertToRPN(expression, null, null, null, true);
        assertNumberToken(tokens[0], new Number(2d));
        assertNumberToken(tokens[1], new Number(2d));
        assertOperatorToken(tokens[2], "-", 1, Operator.PRECEDENCE_UNARY_MINUS);
        assertOperatorToken(tokens[3], "^", 2, Operator.PRECEDENCE_POWER);
    }

    @Test
    public void testShuntingYard6() {
        String expression = "2^---+2";
        Token[] tokens = ShuntingYard.convertToRPN(expression, null, null, null, true);
        assertNumberToken(tokens[0], new Number(2d));
        assertNumberToken(tokens[1], new Number(2d));
        assertOperatorToken(tokens[2], "+", 1, Operator.PRECEDENCE_UNARY_PLUS);
        assertOperatorToken(tokens[3], "-", 1, Operator.PRECEDENCE_UNARY_MINUS);
        assertOperatorToken(tokens[4], "-", 1, Operator.PRECEDENCE_UNARY_MINUS);
        assertOperatorToken(tokens[5], "-", 1, Operator.PRECEDENCE_UNARY_MINUS);
        assertOperatorToken(tokens[6], "^", 2, Operator.PRECEDENCE_POWER);
    }

    @Test
    public void testShuntingYard7() {
        String expression = "2^-2!";
        Operator factorial = getFactorialOperator();

        Map<String, Operator> userOperators = new HashMap<>();
        userOperators.put("!", factorial);
        Token[] tokens = ShuntingYard.convertToRPN(expression, null, userOperators, null, true);
        assertNumberToken(tokens[0], new Number(2d));
        assertNumberToken(tokens[1], new Number(2d));
        assertOperatorToken(tokens[2], "!", 1, Operator.PRECEDENCE_POWER + 1);
        assertOperatorToken(tokens[3], "-", 1, Operator.PRECEDENCE_UNARY_MINUS);
        assertOperatorToken(tokens[4], "^", 2, Operator.PRECEDENCE_POWER);
    }

    @Test
    public void testShuntingYard8() {
        String expression = "-3^2";
        Token[] tokens = ShuntingYard.convertToRPN(expression, null, null, null, true);
        assertNumberToken(tokens[0], new Number(3d));
        assertNumberToken(tokens[1], new Number(2d));
        assertOperatorToken(tokens[2], "^", 2, Operator.PRECEDENCE_POWER);
        assertOperatorToken(tokens[3], "-", 1, Operator.PRECEDENCE_UNARY_MINUS);
    }

    @Test
    public void testShuntingYard9() {
        Operator reciprocal = new Operator("$", 1, true, Operator.PRECEDENCE_DIVISION) {
            @Override
            public Operand apply(final Operand... args) {
                Number arg = (Number) args[0];
                if (arg.getValue() == 0d){
                    throw new ArithmeticException("Division by zero!");
                }
                return new Number(1d / arg.getValue());
//                if (args[0] == 0d) {
//                    throw new ArithmeticException("Division by zero!");
//                }
//                return 1d / args[0];
            }
        };
        Map<String, Operator> userOperators = new HashMap<>();
        userOperators.put("$", reciprocal);
        Token[] tokens = ShuntingYard.convertToRPN("1$", null, userOperators, null, true);
        assertNumberToken(tokens[0], new Number(1d));
        assertOperatorToken(tokens[1], "$", 1, Operator.PRECEDENCE_DIVISION);
    }

}
