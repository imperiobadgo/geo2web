package arithmetic;

import de.geo2web.arithmetic.*;
import de.geo2web.arithmetic.Number;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class NumberArithmeticOperationTests {

    @Test
    public void AddNumbersTest1() {
        float expected = 10;

        Number a = new Number(3);
        Number b = new Number(7);
        ExpressionNode node = new ExpressionNode(a, b, Operator.PLUS);
        Expression result = node.evaluate();

        Assert.isInstanceOf(Number.class, result);
        Assert.isTrue(((Number) result).getValue() == expected, "Expected");
    }

    @Test
    public void AddNumbersTest2() {
        float expected = 10;

        Number a = new Number(2);
        Number b = new Number(3);
        Number c = new Number(5);
        ExpressionNode node1 = new ExpressionNode(a, b, Operator.PLUS);
        ExpressionNode node2 = new ExpressionNode(node1, c, Operator.PLUS);
        Expression result = node2.evaluate();

        Assert.isInstanceOf(Number.class, result);
        Assert.isTrue(((Number) result).getValue() == expected, "Expected");
    }

    @Test
    public void SubtractNumbersTest1() {
        float expected = -4;

        Number a = new Number(3);
        Number b = new Number(7);
        ExpressionNode node = new ExpressionNode(a, b, Operator.MINUS);
        Expression result = node.evaluate();

        Assert.isInstanceOf(Number.class, result);
        Assert.isTrue(((Number) result).getValue() == expected, "Expected");
    }

    @Test
    public void SubtractNumbersTest2() {
        float expected = 0;

        Number a = new Number(2);
        Number b = new Number(3);
        Number c = new Number(5);
        ExpressionNode node1 = new ExpressionNode(a, b, Operator.PLUS);
        ExpressionNode node2 = new ExpressionNode(node1, c, Operator.MINUS);
        Expression result = node2.evaluate();

        Assert.isInstanceOf(Number.class, result);
        Assert.isTrue(((Number) result).getValue() == expected, "Expected");
    }

}
