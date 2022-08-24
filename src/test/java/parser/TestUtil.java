package parser;

import de.geo2web.parser.Number;
import de.geo2web.parser.Operand;
import de.geo2web.parser.operator.Operator;
import de.geo2web.parser.tokenizer.*;
import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TestUtil {

    public static void assertVariableToken(Token token, String name) {
        assertEquals(Token.TOKEN_VARIABLE, token.getType());
        Assert.assertEquals(name, ((VariableToken) token).getName());
    }

    public static void assertOpenParenthesesToken(Token token) {
        assertEquals(Token.TOKEN_PARENTHESES_OPEN, token.getType());
    }

    public static void assertCloseParenthesesToken(Token token) {
        assertEquals(Token.TOKEN_PARENTHESES_CLOSE, token.getType());
    }

    public static void assertFunctionToken(Token token, String name, int i) {
        assertEquals(token.getType(), Token.TOKEN_FUNCTION);
        FunctionToken f = (FunctionToken) token;
        assertEquals(i, f.getFunction().getNumArguments());
        assertEquals(name, f.getFunction().getName());
    }

    public static void assertOperatorToken(Token tok, String symbol, int numArgs, int precedence) {
        assertEquals(tok.getType(), Token.TOKEN_OPERATOR);
        Assert.assertEquals(numArgs, ((OperatorToken) tok).getOperator().getNumOperands());
        assertEquals(symbol, ((OperatorToken) tok).getOperator().getSymbol());
        assertEquals(precedence, ((OperatorToken) tok).getOperator().getPrecedence());
    }

    public static void assertNumberToken(Token tok, Number v) {
        assertEquals(tok.getType(), Token.TOKEN_NUMBER);
        Assert.assertEquals(v.getValue(), ((NumberToken) tok).getValue().getValue(), 0f);
    }

    public static void assertFunctionSeparatorToken(Token t) {
        assertEquals(t.getType(), Token.TOKEN_SEPARATOR);
    }

    public static Operator getFactorialOperator() {
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
//                final int arg = (int) args[0];
//                if ((double) arg != args[0]) {
//                    throw new IllegalArgumentException("Operand for factorial has to be an integer");
//                }
//                if (arg < 0) {
//                    throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
//                }
//                double result = 1;
//                for (int i = 1; i <= arg; i++) {
//                    result *= i;
//                }
//                return result;
            }
        };
        return factorial;
    }
}
