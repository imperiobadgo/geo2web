package arithmetic;

import de.geo2web.arithmetic.*;
import de.geo2web.arithmetic.Number;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class NumberVariableTests {

    @Test
    public void AddNumbersTest1() {
        float expected = 10;

        Expression a = new NumberVariable("x").setValue(new Number(3));
        Expression b = new NumberVariable("y").setValue(new Number(7));
        ExpressionNode node = new ExpressionNode(a, b, Operator.PLUS);
        Expression result = node.evaluate();

        Assert.isInstanceOf(Number.class, result);
        Assert.isTrue(((Number) result).getValue() == expected, "Expected");
    }

    @Test
    public void AddNumbersAndChangeTest2() {
        float expected1 = 10;
        float expected2 = 15;

        NumberVariable a = new NumberVariable("x").setValue(new Number(3));
        Expression b = new NumberVariable("y").setValue(new Number(7));
        ExpressionNode node = new ExpressionNode(a, b, Operator.PLUS);
        Expression result1 = node.evaluate();

        Assert.isInstanceOf(Number.class, result1);
        Assert.isTrue(((Number) result1).getValue() == expected1, "Expected1");

        a.setValue(new Number(8));
        Expression result2 = node.evaluate();

        Assert.isInstanceOf(Number.class, result2);
        Assert.isTrue(((Number) result2).getValue() == expected2, "Expected2");
    }

}
