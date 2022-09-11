package arithmetic;

import de.geo2web.arithmetic.ExpressionBuilder;
import de.geo2web.arithmetic.Number;
import de.geo2web.arithmetic.Operand;
import de.geo2web.arithmetic.Vector;
import org.junit.Test;

import static arithmetic.TestUtil.EPSILON;
import static org.junit.Assert.assertEquals;

public class VectorArithmeticOperationTests {

    @Test
    public void addVectorCalc01() {
        Operand result = new ExpressionBuilder("{2,0,5}+5")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(7f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(5f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(10f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void addVectorCalc02() {
        Operand result = new ExpressionBuilder("{2^2,2pi,5}+5")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(9f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(2 * Math.PI + 5, ((Number) operand2).getValue(), EPSILON);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(10f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void addVectorCalc03() {
        Operand result = new ExpressionBuilder("{2,0,5}+{2,-5,5}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(4f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(-5f, ((Number) operand2).getValue(), EPSILON);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(10f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void dotVectorCalc01() {
        Operand result = new ExpressionBuilder("{1,0,0}*{1,0,0}")
                .build()
                .evaluate();
        assertEquals(1f, ((Number) result).getValue(), 0f);
    }

    @Test
    public void dotVectorCalc02() {
        Operand result = new ExpressionBuilder("{1,0,0}*{1,1,0}")
                .build()
                .evaluate();
        assertEquals(1f, ((Number) result).getValue(), 0f);
    }

    @Test
    public void dotVectorCalc03() {
        Operand result = new ExpressionBuilder("{3,0,0}*{1,1,0}")
                .build()
                .evaluate();
        assertEquals(3f, ((Number) result).getValue(), 0f);
    }

    @Test
    public void dotVectorCalc04() {
        Operand result = new ExpressionBuilder("{1,2,3}*{1,2,3}")
                .build()
                .evaluate();
        assertEquals(14f, ((Number) result).getValue(), 0f);
    }

    @Test(expected = ArithmeticException.class)
    public void dotVector_NotSameDimensions() {
        new ExpressionBuilder("{1,2,3,4}*{1,2,3}")
                .build()
                .evaluate();
    }

    @Test
    public void powVectorCalc01() {
        Operand result = new ExpressionBuilder("{1,2,3}^2")
                .build()
                .evaluate();
        assertEquals(14f, ((Number) result).getValue(), 0f);
    }

    @Test
    public void powVectorCalc02() {
        Operand result = new ExpressionBuilder("{1,2,3}^3")
                .build()
                .evaluate();
        float dotResult = 14f;
        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(dotResult, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(2f * dotResult, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(3f * dotResult, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void powVectorCalc03() {
        Operand result = new ExpressionBuilder("{1,2,3}^4")
                .build()
                .evaluate();
        assertEquals(14f * 14f, ((Number) result).getValue(), 0f);
    }

    @Test
    public void powVectorCalc04() {
        Operand result = new ExpressionBuilder("{1,2,3}^5")
                .build()
                .evaluate();
        float dotResult = 14f * 14f;
        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(dotResult, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(2f * dotResult, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(3f * dotResult, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void powVectorCalc05() {
        Operand result = new ExpressionBuilder("{1,2,3}^4.3")
                .build()
                .evaluate();
        assertEquals(14f * 14f, ((Number) result).getValue(), 0f);
    }

    @Test(expected = ArithmeticException.class)
    public void powVector_NotSupported() {
        new ExpressionBuilder("{1,2,3}^1")
                .build()
                .evaluate();
    }

    @Test
    public void crossVectorCalc01() {
        Operand result = new ExpressionBuilder("cross({1,0,0},{0,1,0})")
                .build()
                .evaluate();
        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(0f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(0f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(1f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void crossVectorCalc02() {
        Operand result = new ExpressionBuilder("cross({1,0,0},{0,-1,0})")
                .build()
                .evaluate();
        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(0f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(0f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(-1f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void crossVectorCalc03() {
        Operand result = new ExpressionBuilder("cross({1,2,3},{-7,8,9})")
                .build()
                .evaluate();
        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-6f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(-30f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(22f, ((Number) operand3).getValue(), 0f);
    }

    @Test(expected = ArithmeticException.class)
    public void crossVector_A_NotSupported() {
        new ExpressionBuilder("cross({1,0,0,-4},{0,2,0})")
                .build()
                .evaluate();
    }

    @Test(expected = ArithmeticException.class)
    public void crossVector_B_NotSupported() {
        new ExpressionBuilder("cross({1,0,0},{0,2,0,5})")
                .build()
                .evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void crossVector_OperandsNotSupported() {
        new ExpressionBuilder("cross({1,0,0},4)")
                .build()
                .evaluate();
    }

    @Test
    public void lengthVectorCalc01() {
        Operand result = new ExpressionBuilder("length({1,0,0})")
                .build()
                .evaluate();
        assertEquals(1f, ((Number) result).getValue(), 0f);
    }

    @Test
    public void lengthVectorCalc02() {
        Operand result = new ExpressionBuilder("length({1,1,0})")
                .build()
                .evaluate();
        assertEquals(Math.sqrt(2), ((Number) result).getValue(), EPSILON);
    }

    @Test
    public void lengthVectorCalc03() {
        Operand result = new ExpressionBuilder("length({5,-5,0})")
                .build()
                .evaluate();
        assertEquals(Math.sqrt(2) * 5, ((Number) result).getValue(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void lengthVector_OperandNotSupported() {
        new ExpressionBuilder("length(-25)")
                .build()
                .evaluate();
    }

    @Test
    public void multVectorCalc01() {
        Operand result = new ExpressionBuilder("{1,2,3}*5")
                .build()
                .evaluate();
        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(5f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(10f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(15f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void multVectorCalc02() {
        Operand result = new ExpressionBuilder("-3 *{1,2,3}")
                .build()
                .evaluate();
        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-3f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(-6f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(-9f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void multVectorCalc03() {
        Operand result = new ExpressionBuilder("0*{1,2,3}")
                .build()
                .evaluate();
        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(0f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(0f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(0f, ((Number) operand3).getValue(), 0f);
    }
}
